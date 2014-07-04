package kr.co.adflow.push.bsbank.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.push.bsbank.mapper.UserMapper;
import kr.co.adflow.push.dao.SMSDao;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Sms;
import kr.co.adflow.push.domain.bsbank.User;
import kr.co.adflow.push.exception.NonExistentUserException;
import kr.co.adflow.push.exception.PhoneNumberNotFoundException;
import kr.co.adflow.push.mapper.MessageMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * sms메시지처리
 * 
 * @author nadir93
 * @date 2014. 6. 12.
 */
@Component
public class SMSHandler implements Runnable {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SMSHandler.class);

	private static boolean first = true;

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	@Qualifier("bsBanksqlSession")
	private SqlSession bsBanksqlSession;

	@Autowired
	private SMSDao smsDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("SMSPrecessing " + orgName);
			first = !first;
		}

		logger.debug("SMS핸들러처리시작()");
		// db query select sms
		// send sms
		// db update

		try {
			MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
			// select * from message
			// where status = 1 and sms = 1 and issue is not
			// null and issueSms is null
			List<Message> list = (List<Message>) msgMapper
					.getUndeliveredSmsMsg();
			for (Message msg : list) {
				logger.debug("msg=" + msg);

				Calendar cal = Calendar.getInstance();
				cal.setTime(msg.getIssue());
				cal.add(cal.MINUTE, msg.getTimeOut());
				Date timeOut = cal.getTime();
				logger.debug("타임아웃시간=" + timeOut);
				// 발송시간(issue)과 현재시간 그리고 timeOut을 계산하여 대상선정
				if (timeOut.before(new Date())) {
					// SMS발송대상인 경우
					logger.debug("SMS전송대상입니다.");
					int msgType = msg.getType();

					switch (msgType) {
					case Message.NOTIFICATION_PERSONAL: // 개인메시지
						logger.debug("개인메시지처리시작");
						Acknowledge ack = new Acknowledge();
						ack.setId(msg.getId());
						ack.setUserID(msg.getReceiver().substring(7));
						// ack 존재유무 파악
						if (msgMapper.getAck(ack)) {
							// ack가 존재하면 sms 전송 종료
							logger.debug("ack메시지가존재합니다.");
							// db(status) update
							msg.setStatus(Message.STATUS_EXIST_ACK);
							msgMapper.putStatus(msg);
							logger.debug("메시지정보를완료로업데이트했습니다.");
						} else {
							// ack가 존재하지 않으면 sms 전송후 종료
							try {
								sendSMS(msgMapper, ack.getUserID(), msg);
								// 전송후 db(issueSms, status) update
								msg.setIssueSms(new Date());
								msg.setStatus(Message.STATUS_SMS_SENT);
								msgMapper.putIssueSms(msg);
								logger.debug("전송시간정보를업데이트했습니다.");
								// db sms update
								Sms sms = new Sms();
								sms.setId(ack.getId());
								sms.setUserID(ack.getUserID());
								sms.setIssue(new Date());
								sms.setStatus(Sms.SMS_SENT);
								msgMapper.postSms(sms);
								logger.debug("SMS발송정보가업데이트되었습니다.");
							} catch (NonExistentUserException e) {
								logger.debug(e.getMessage());
								// db(status) update
								msg.setStatus(Message.STATUS_USER_NOT_FOUND);
								msgMapper.putStatus(msg);
								logger.debug("메시지정보를업데이트했습니다.");
							} catch (PhoneNumberNotFoundException e) {
								logger.debug(e.getMessage());
								// db(status) update
								msg.setStatus(Message.STATUS_PHONENUMBER_NOT_FOUND);
								msgMapper.putStatus(msg);
								logger.debug("메시지정보를업데이트했습니다.");
								// throw e;
							}
						}
						logger.debug("개인메시지처리종료");
						break;
					case Message.NOTIFICATION_ALL: // 전체메시지
						logger.debug("전체메시지처리시작");
						// 전체사용자가져오기
						UserMapper userMapper = bsBanksqlSession
								.getMapper(UserMapper.class);
						User[] allUsers = userMapper.getAllUser();
						processSMS(msgMapper, msg, allUsers);
						logger.debug("전체메시지처리종료");
						break;
					case Message.NOTIFICATION_GROUP_AFFILIATE: // 그룹메시지(계열사)
						logger.debug("그룹메시지(계열사)처리시작");
						// 계열사그룹사용자가져오기
						UserMapper usrMapper = bsBanksqlSession
								.getMapper(UserMapper.class);
						User[] alliliateUser = usrMapper.getUsersBySBSD(msg
								.getReceiver().substring(8));
						processSMS(msgMapper, msg, alliliateUser);
						logger.debug("그룹메시지(계열사)처리종료");
						break;
					case Message.NOTIFICATION_GROUP_DEPT: // 그룹메시지(부서)
						logger.debug("그룹메시지(부서)처리시작");
						// 부서그룹사용자가져오기
						UserMapper mapper = bsBanksqlSession
								.getMapper(UserMapper.class);
						User[] deptUser = mapper.getUsersByDepartment(msg
								.getReceiver().substring(8));
						processSMS(msgMapper, msg, deptUser);
						logger.debug("그룹메시지(부서)처리종료");
						break;
					default:
						logger.debug("메시지타입오류입니다.");
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
		logger.debug("SMS핸들러처리종료()");
	}

	private void processSMS(MessageMapper msgMapper, Message msg, User[] users)
			throws Exception {
		// ack사용자가져오기
		Acknowledge[] acks = msgMapper.getAckAll(msg.getId());

		Map<String, Acknowledge> ackMap = new HashMap<String, Acknowledge>();
		for (Acknowledge acknowledge : acks) {
			logger.debug("acknowledge=" + acknowledge);
			ackMap.put(acknowledge.getUserID(), acknowledge);
		}

		int smsCount = 0;
		for (User user : users) {
			try {
				logger.debug("user=" + user);
				// sms 테이블을 체크해보고 없으면 수행 있으면 스킵
				Sms sms = new Sms();
				sms.setId(msg.getId());
				sms.setUserID(user.getGw_stf_cdnm());
				if (msgMapper.getSms(sms)) {
					// skip
					logger.debug("이미SMS처리작업이완료되었습니다.");
				} else {
					// 루프돌면서 각사용자의 ack정보에 따라 메시지 전송
					if (ackMap.containsKey(user.getGw_stf_cdnm())) {
						// skip
						logger.debug("ack메시지가있습니다.(sms발송대상이아닙니다) user="
								+ user.getGw_stf_cdnm());
					} else {
						// sms 전송
						try {
							sendSMS(msgMapper, user.getGw_stf_cdnm(), msg);
							smsCount++;
							// db sms update
							sms.setIssue(new Date());
							sms.setStatus(Sms.SMS_SENT);
							msgMapper.postSms(sms);
							logger.debug("SMS발송정보가업데이트되었습니다.(전송되었습니다.)");
						} catch (NonExistentUserException e) {
							logger.debug(e.getMessage());
							// db(status) update
							sms.setStatus(Sms.STATUS_USER_NOT_FOUND);
							msgMapper.postSms(sms);
							logger.debug("SMS발송정보가업데이트되었습니다.(사용자가없습니다.)");
							// throw e;
						} catch (PhoneNumberNotFoundException e) {
							logger.debug(e.getMessage());
							// db(status) update
							sms.setStatus(Sms.STATUS_PHONENUMBER_NOT_FOUND);
							msgMapper.postSms(sms);
							logger.debug("SMS발송정보가업데이트되었습니다.(전화번호가없습니다.)");
							// throw e;
						}
					}
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
		}

		// 전송후 db update
		if (smsCount > 0) {
			msg.setIssueSms(new Date());
			msg.setStatus(Message.STATUS_SMS_SENT);
			msgMapper.putIssueSms(msg);
			logger.debug("전송시간정보를업데이트했습니다.status=" + Message.STATUS_SMS_SENT);
		} else {
			msg.setStatus(Message.STATUS_SMS_PROCESS_DONE);
			msgMapper.putStatus(msg);
			logger.debug("SMS처리작업정보를업데이트했습니다.status="
					+ Message.STATUS_SMS_PROCESS_DONE);
		}
	}

	private void sendSMS(MessageMapper msgMapper, String userID, Message msg)
			throws Exception {
		logger.debug("sendSMS시작(msg=" + msg + ")");
		UserMapper userMapper = bsBanksqlSession.getMapper(UserMapper.class);
		User user = userMapper.get(userID);
		logger.debug("user=" + user);

		if (user == null) {
			throw new NonExistentUserException("푸시사용자가존재하지않습니다.userID="
					+ userID);
		}

		// Message message = msgMapper.get(msg.getId());
		// 80문자이고 내용을 적절히 변경해야함
		// 필요정보는 전화번호 ...
		if (user.getMpno() != null) {
			smsDao.post(user.getMpno(), "15441000", msg.getSender()
					+ "님으로부터 메시지가도착하였습니다. 메시지앱으로확인바랍니다.");
		} else {
			throw new PhoneNumberNotFoundException("전화번호가존재하지않습니다.userID="
					+ userID);
		}
		// mqttService.publish(message);
		logger.debug("SMS메시지를전송하였습니다.");
	}
}