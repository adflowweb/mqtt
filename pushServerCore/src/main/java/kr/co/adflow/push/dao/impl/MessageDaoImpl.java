package kr.co.adflow.push.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.dao.SMSDao;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Sms;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.exception.NonExistentUserException;
import kr.co.adflow.push.exception.PhoneNumberNotFoundException;
import kr.co.adflow.push.mapper.MessageMapper;
import kr.co.adflow.push.mapper.UserMapper;
import kr.co.adflow.push.service.MqttService;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
// @Component
public class MessageDaoImpl implements MessageDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageDaoImpl.class);

	private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MessageDaoImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Resource
	MqttService mqttService;

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private SMSDao smsDao;

	private ScheduledExecutorService smsLooper;
	private ScheduledExecutorService messageLooper;

	// 메시지처리 유무
	private boolean msgProcess = Boolean.parseBoolean(prop
			.getProperty("message.enable"));
	// 메시지처리주기
	private int messageInterval = Integer.parseInt(prop
			.getProperty("message.process.interval"));
	// SMS처리 유무
	private boolean sms = Boolean.parseBoolean(prop.getProperty("sms.enable"));
	// sms처리주기
	private int smsInterval = Integer.parseInt(prop
			.getProperty("sms.process.interval"));

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("MessageDAOImpl초기화시작()");

		logger.info("메시지처리유무=" + msgProcess);
		if (msgProcess) {
			messageLooper = Executors.newScheduledThreadPool(1);
			messageLooper.scheduleWithFixedDelay(new MessageHandler(),
					messageInterval, messageInterval, TimeUnit.SECONDS);
			logger.info("메시지핸들러가시작되었습니다.");
		}

		logger.info("SMS처리유무=" + sms);
		if (sms) {
			smsLooper = Executors.newScheduledThreadPool(1);
			smsLooper.scheduleWithFixedDelay(new SMSHandler(), smsInterval,
					smsInterval, TimeUnit.SECONDS);
			logger.info("SMS핸들러가시작되었습니다.");
		}
		logger.info("MessageDAOImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");

		if (msgProcess) {
			messageLooper.shutdown();
			logger.info("메시지핸들러가종료되었습니다.");
		}

		if (sms) {
			smsLooper.shutdown();
			logger.info("SMS핸들러가종료되었습니다.");
		}

		logger.info("cleanUp종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#get(java.lang.String)
	 */
	@Override
	public Message get(int msgID) throws Exception {
		logger.debug("get시작(msgID=" + msgID + ")");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message msg = msgMapper.get(msgID);
		logger.debug("get종료(" + msg + ")");
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#post(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public int post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.postMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.postContent(msg);
		logger.debug("post종료(count=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public int put(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.putMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.putContent(msg);
		logger.debug("put종료(count=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public int delete(int msgID) throws Exception {
		logger.debug("delete시작(msgID=" + msgID + ")");
		MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
		int result = messageMapper.deleteMsg(msgID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	@Override
	public Message[] getReservationMsgs() throws Exception {
		logger.debug("getReservationMsgs시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message[] msg = msgMapper.getReservationMsgs();
		logger.debug("getReservationMsgs종료(" + msg + ")");
		return msg;
	}

	/**
	 * 메시지처리
	 * 
	 * @author nadir93
	 * @date 2014. 6. 12.
	 */
	class MessageHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("메시지처리시작()");

			try {
				String errMsg = mqttService.getErrorMsg();
				if (errMsg == null) {
					// mqtt connection이 정상일때만 처리함
					MessageMapper msgMapper = sqlSession
							.getMapper(MessageMapper.class);
					List<Message> list = (List<Message>) msgMapper
							.getUndeliveredMsg();
					for (Message msg : list) {
						logger.debug("msg=" + msg);
						if (msg.getReservation() == null) {
							// 아이폰 안드로이드 구별하여야함
							// 아이폰일경우 전체 메시지 or 그룹메시지와 상관없이 apns로 전송해야함
							// 즉시전송메시지
							logger.debug("즉시전송대상입니다.");
							publish(msgMapper, msg);
						} else {
							// 예약메시지
							if (msg.getReservation().before(new Date())) {
								logger.debug("예약전송대상입니다.");
								publish(msgMapper, msg);
							}
						}
					}
				} else {
					logger.error("mqtt서비스에문제가있어메시지처리를skip합니다.errMsg=" + errMsg);
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
			logger.debug("메시지처리종료()");
		}

		private void publish(MessageMapper msgMapper, Message msg)
				throws Exception {
			Message message = msgMapper.get(msg.getId());
			mqttService.publish(message);
			logger.debug("메시지를전송하였습니다.");
			// 모니터링용 송신 메시지 처리건수 계산 추가해야함

			// 전송후 db(issue) update
			msg.setIssue(new Date());
			msgMapper.putIssue(msg);
			logger.debug("전송시간정보를업데이트했습니다.");
		}
	}

	/**
	 * sms메시지처리
	 * 
	 * @author nadir93
	 * @date 2014. 6. 12.
	 */
	class SMSHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("SMS핸들러처리시작()");
			// db query select sms
			// send sms
			// db update

			try {
				MessageMapper msgMapper = sqlSession
						.getMapper(MessageMapper.class);
				// select * from message
				// where status = 0 and sms = 1 and issue is not
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
						case 0: // 개인메시지
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
									msgMapper.putMsg(msg);
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
									msgMapper.putMsg(msg);
									logger.debug("메시지정보를업데이트했습니다.");
								} catch (PhoneNumberNotFoundException e) {
									logger.debug(e.getMessage());
									// db(status) update
									msg.setStatus(Message.STATUS_PHONENUMBER_NOT_FOUND);
									msgMapper.putMsg(msg);
									logger.debug("메시지정보를업데이트했습니다.");
									// throw e;
								}
							}
							logger.debug("개인메시지처리종료");
							break;
						case 1: // 그룹메시지
							logger.debug("그룹메시지처리시작");
							logger.debug("그룹메시지처리종료");
							break;
						case 2: // 전체메시지
							logger.debug("전체메시지처리시작");
							// 전체사용자가져오기
							UserMapper userMapper = sqlSession
									.getMapper(UserMapper.class);
							User[] users = userMapper.getAllUser();
							// ack사용자가져오기
							Acknowledge[] acks = msgMapper.getAckAll(msg
									.getId());

							Map<String, Acknowledge> ackMap = new HashMap<String, Acknowledge>();
							for (Acknowledge acknowledge : acks) {
								logger.debug("acknowledge=" + acknowledge);
								ackMap.put(acknowledge.getUserID(), acknowledge);
							}

							for (User user : users) {
								logger.debug("user=" + user);
								// sms 테이블을 체크해보고 없으면 수행 있으면 스킵
								Sms sms = new Sms();
								sms.setId(msg.getId());
								sms.setUserID(user.getUserID());
								if (msgMapper.getSms(sms)) {
									// skip
									logger.debug("이미SMS가발송되었습니다.");
								} else {
									// 루프돌면서 각사용자의 ack정보에 따라 메시지 전송
									if (ackMap.containsKey(user.getUserID())) {
										// skip
										logger.debug("ack메시지가있습니다.(sms발송대상이아닙니다) user="
												+ user.getUserID());
									} else {
										// sms 전송
										try {
											sendSMS(msgMapper,
													user.getUserID(), msg);
											// db sms update
											sms.setIssue(new Date());
											sms.setStatus(Sms.SMS_SENT);
											msgMapper.postSms(sms);
											logger.debug("SMS발송정보가업데이트되었습니다.");
										} catch (NonExistentUserException e) {
											logger.debug(e.getMessage());
											// db(status) update
											sms.setStatus(Sms.STATUS_USER_NOT_FOUND);
											msgMapper.postSms(sms);
											logger.debug("SMS발송정보가업데이트되었습니다.");
											// throw e;
										} catch (PhoneNumberNotFoundException e) {
											logger.debug(e.getMessage());
											// db(status) update
											sms.setStatus(Sms.STATUS_PHONENUMBER_NOT_FOUND);
											msgMapper.postSms(sms);
											logger.debug("SMS발송정보가업데이트되었습니다.");
											// throw e;
										}
									}
								}
							}

							// 전송후 db update
							msg.setIssueSms(new Date());
							msg.setStatus(Message.STATUS_SMS_SENT);
							msgMapper.putMsg(msg);
							logger.debug("전송시간정보를업데이트했습니다.");
							logger.debug("전체메시지처리종료");
							break;
						case 100: // subscribe
						case 101: // unsubscribe
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

		private void sendSMS(MessageMapper msgMapper, String userID, Message msg)
				throws Exception {
			logger.debug("sendSMS시작(msg=" + msg + ")");
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			User user = userMapper.get(userID);
			logger.debug("user=" + user);

			if (user == null) {
				throw new NonExistentUserException("푸시사용자가존재하지않습니다.userID="
						+ userID);
			}

			// Message message = msgMapper.get(msg.getId());
			// 80문자이고 내용을 적절히 변경해야함
			// 필요정보는 전화번호 ...
			if (user.getPhone() != null) {
				smsDao.post(user.getPhone(), "15441000", msg.getSender()
						+ "님으로부터 메시지가도착하였습니다. 메시지앱으로확인바랍니다.");
			} else {
				throw new PhoneNumberNotFoundException("전화번호가존재하지않습니다.userID="
						+ userID);
			}
			// mqttService.publish(message);
			logger.debug("SMS메시지를전송하였습니다.");
		}
	}
}
