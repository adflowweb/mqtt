/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.AckCallback;
import kr.co.adflow.pms.domain.CtlQ;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.AckMapper;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.push.mapper.ValidationMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.svc.request.CallbackReq;
import kr.co.adflow.pms.svc.service.PushMessageService;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageSendServiceImpl.
 */
@Service
public class MessageSendServiceImpl implements MessageSendService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendServiceImpl2.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The push message service. */
	@Autowired
	private PushMessageService pushMessageService;

	/** The jms template. */
	@Autowired
	private JmsTemplate jmsTemplate;

	/** The validation mapper. */
	@Autowired
	private ValidationMapper validationMapper;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	/** The ctl q mapper. */
	@Autowired
	private CtlQMapper ctlQMapper;

	/** The ack mapper. */
	@Autowired
	private AckMapper ackMapper;

	/** The rest template. */
	@Autowired
	RestTemplate restTemplate;

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.core.service.MessageSendService#sendMessageArray(java.lang.String, int)
	 */
	@Override
	public int sendMessageArray(String serverId, int limit) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		// param.put("keyMon", DateUtil.getYYYYMM());
		param.put("keyMon", this.getMessageTableName(serverId));
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectList(param);

		
		//logger.info("list cnt :: {}", list.size());

		int updateCnt = 0;
		for (Message msg : list) {

			msg.setKeyMon(this.getKeyMon(msg.getMsgId()));

			boolean isUserMessage = true;
			if (msg.getReceiverTopic() == null
					|| msg.getReceiverTopic().trim().length() == 0) {
				// admin 이 보낸 메시지는 topic 있는지 확인 안함
				// msg.getReceiverTopic(); 처리
				isUserMessage = true;
				if (!this.validReceiverUserId(msg.getReceiver())) {
					logger.debug("============= MESSAGE_STATUS_RECEIVER_NOT_FOUNT");
					msg.setStatus(StaticConfig.MESSAGE_STATUS_RECEIVER_NOT_FOUNT);
					messageMapper.updateStatus(msg);
					ctlQMapper.deleteQ(msg.getMsgId());
					continue;
				}

				msg.setReceiverTopic(this.getReceiverTopic(msg.getReceiver()));

			} else {
				isUserMessage = false;
				// msg.getReceiverTopic() 이미 있음
				logger.info("Message Receiver topic name :",
						msg.getReceiverTopic());
			}

			if (isUserMessage
					&& userMapper.getMsgCntLimit(msg.getIssueId()) < 1) {
				msg.setStatus(StaticConfig.MESSAGE_STATUS_COUNT_OVER);
				messageMapper.updateStatus(msg);
				ctlQMapper.deleteQ(msg.getMsgId());
				continue;
			}

//			kicho-20150420:jms pool update [start]			
//			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(msg));
			jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate,msg));
//			kicho-20150420:jms pool update [end]	
			
			// 재전송 로직 추가
			String msgId = msg.getMsgId();
			if (msg.getResendMaxCount() > 0) {
				
				this.reservationResend(msg,msgId);
			}
			msg.setMsgId(msgId);
			// msg.getReceiverTopic();
			msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);

			int resultCnt = messageMapper.updateStatus(msg);
			ctlQMapper.deleteQ(msg.getMsgId());
			//logger.info("update count is {}", resultCnt);
			// message_cnt - result_count
			User user = new User();
			user.setUserId(msg.getIssueId());
			user.setMsgCntLimit(resultCnt);
			if (isUserMessage) {
				userMapper.discountMsgCntLimit(user);
			}
			updateCnt++;
		}
		if (updateCnt > 0)
		logger.info("sendMessageArray {} is cnt {}", serverId,updateCnt);
		return updateCnt;
	}

	private void reservationResend(Message msg,String msgId) {

		long reservationTime = 0L;
		if (msg.getReservationTime() == null) {
			reservationTime = System.currentTimeMillis();
		} else {
			reservationTime = msg.getReservationTime().getTime();	
		}
		int resendCnt = 1;
		for (int i = 0; i < msg.getResendMaxCount(); i++) {
			int intervalM = msg.getResendInterval();
			msg.setReservation(true);
			long interval = intervalM * 1000 * 60;
			reservationTime = reservationTime + interval;
			msg.setReservationTime(new Date(reservationTime));
			msg.setMsgId(this.getMsgId());
			msg.setResendCount(resendCnt++);
			msg.setResendId(msgId);
			
//			messageMapper.insertMessage(msg);
//			messageMapper.insertContent(msg);
			messageMapper.insertReservationMessage(msg);

			ctlQMapper.insertQ(this.getCtlQ(msg));

		}
		
	}

	/**
	 * Gets the message table name.
	 *
	 * @param serverId the server id
	 * @return the message table name
	 */
	private String getMessageTableName(String serverId) {
		CtlQ result = null;
		CtlQ paramCtlQ = new CtlQ();
		paramCtlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE);
		paramCtlQ.setServerId(serverId);
		result = ctlQMapper.fetchQ(paramCtlQ);

		if (result == null) {
			return DateUtil.getYYYYMM();
		} else {
			return result.getTableName();
		}
	}

	/**
	 * Valid receiver user id.
	 *
	 * @param receiver the receiver
	 * @return true, if successful
	 */
	private boolean validReceiverUserId(String receiver) {
		logger.debug("=== receiver::"+receiver);
		boolean result = false;

		int type = userValidator.getRequestType(receiver);
		
		logger.debug("=== type::"+type);

		if (StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_PHONE == type) {
			result = validationMapper.validPhoneNo(userValidator
					.getRegstPhoneNo(receiver));
			logger.debug("=== userValidator.getRegstPhoneNo::"+userValidator.getRegstPhoneNo(receiver));
			logger.debug("===TYPE_PHONE result::"+result);
		}

		if (StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1 == type
				|| StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2 == type) {
			result = validationMapper.validUfmiNo(userValidator
					.getRegstUfmiNo(receiver));
			logger.debug("===TYPE_UFMI result::"+result);
		}

		
		logger.debug("=== return result::"+result);
		return result;

	}

	/**
	 * Gets the ctl q.
	 *
	 * @param msg the msg
	 * @return the ctl q
	 */
	private CtlQ getCtlQ(Message msg) {
		CtlQ ctlQ = new CtlQ();

		if (msg.isReservation()) {
			ctlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION);
			ctlQ.setIssueTime(msg.getReservationTime());
		} else {
			ctlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE);
			ctlQ.setIssueTime(new Date());
		}

		ctlQ.setTableName(msg.getKeyMon());
		ctlQ.setMsgId(msg.getMsgId());
		ctlQ.setServerId(msg.getServerId());

		return ctlQ;
	}

//	private boolean isPhoneNo(String receiver) {
//		if ("010".equals(receiver.substring(0, 3))) {
//			return true;
//		} else {
//			return false;
//		}
//
//	}

	/**
 * Gets the key mon.
 *
 * @param string the string
 * @return the key mon
 */
private String getKeyMon(String string) {
		String result = string.substring(0, 6);
		logger.debug("getKeyMon is {}", result);
		return result;
	}

	/**
	 * Gets the msg id.
	 *
	 * @return the msg id
	 */
	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

	/**
	 * Gets the receiver topic.
	 *
	 * @param receiver the receiver
	 * @return the receiver topic
	 */
	private String getReceiverTopic(String receiver) {

		String result = null;

		int type = userValidator.getRequestType(receiver);

		if (StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_PHONE == type) {
			result = userValidator.getSubscribPhoneNo(receiver);
		}

		if (StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1 == type) {
			result = userValidator.getSubscribUfmi1(receiver);
		}

		if (StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2 == type) {
			result = userValidator.getSubscribUfmi2(receiver);
		}

		return result;
	}

//	/**
//	 * Gets the reservation table name.
//	 *
//	 * @param serverId the server id
//	 * @return the reservation table name
//	 */
//	private String getReservationTableName(String serverId) {
//		CtlQ result = null;
//		CtlQ paramCtlQ = new CtlQ();
//		paramCtlQ
//				.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION);
//		paramCtlQ.setServerId(serverId);
//		result = ctlQMapper.fetchQ(paramCtlQ);
//
//		if (result == null) {
//			return DateUtil.getYYYYMM();
//		} else {
//			return result.getTableName();
//		}
//	}

	/**
	 * Gets the callback table name.
	 *
	 * @param serverId the server id
	 * @return the callback table name
	 */
	private String getCallbackTableName(String serverId) {
		CtlQ result = null;
		CtlQ paramCtlQ = new CtlQ();
		paramCtlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK);
		paramCtlQ.setServerId(serverId);
		result = ctlQMapper.fetchQ(paramCtlQ);

		if (result == null) {
			return DateUtil.getYYYYMM();
		} else {
			return result.getTableName();
		}
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.core.service.MessageSendService#sendReservationMessageArray(java.lang.String, int)
	 */
	@Override
	public int sendReservationMessageArray(String serverId, int limit) {
		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("keyMon", DateUtil.getYYYYMM());
//		param.put("keyMon", this.getReservationTableName(serverId));
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectReservationList(param);

		int updateCnt = 0;
		for (Message msg : list) {

			msg.setKeyMon(this.getKeyMon(msg.getMsgId()));

			boolean isUserMessage = true;
			if (msg.getReceiverTopic() == null
					|| msg.getReceiverTopic().trim().length() == 0) { // admin 이
																		// 보낸
																		// 메시지는
																		// topic
																		// 있는지
																		// 확인 안함
				// msg.getReceiverTopic(); 처리
				isUserMessage = true;
				if (!this.validReceiverUserId(msg.getReceiver())) {
					msg.setStatus(StaticConfig.MESSAGE_STATUS_RECEIVER_NOT_FOUNT);
//					messageMapper.updateStatus(msg);
//					ctlQMapper.deleteQ(msg.getMsgId());
					messageMapper.insertMessageRV(msg);
					messageMapper.insertContent(msg);
					messageMapper.deleteReservationMessage(msg.getMsgId());
					continue;
				}

				msg.setReceiverTopic(this.getReceiverTopic(msg.getReceiver()));
			} else {
				isUserMessage = false;
				// msg.getReceiverTopic() 이미 있음
				logger.info("Message Receiver topic name :",msg.getReceiverTopic());
			}

			//20150510 - message count limit logic skip
//			if (isUserMessage
//					&& userMapper.getMsgCntLimit(msg.getIssueId()) < 1) {
//				msg.setStatus(StaticConfig.MESSAGE_STATUS_COUNT_OVER);
//				messageMapper.updateStatus(msg);
//				ctlQMapper.deleteQ(msg.getMsgId());
//				continue;
//			}


//			kicho-20150420:jms pool update [start]			
//			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(msg));
			jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate,msg));
//			kicho-20150420:jms pool update [end]	
			
			
			// 재전송 로직 추가
			String msgId = msg.getMsgId();
			if (msg.getResendMaxCount() > 0 && msg.getResendCount() == 0) {

				this.reservationResend(msg,msgId);
			}


			msg.setMsgId(msgId);
			msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);

//			int resultCnt = messageMapper.updateStatus(msg);
			messageMapper.insertMessageRV(msg);
			messageMapper.insertContent(msg);
			messageMapper.deleteReservationMessage(msgId);
//			ctlQMapper.deleteQ(msg.getMsgId());
			
			//20150510 - message count limit logic skip
//			if (resultCnt>0)
//			logger.info("update count is {}", resultCnt);
//			// message_cnt - result_count
//			User user = new User();
//			user.setUserId(msg.getIssueId());
//			user.setMsgCntLimit(resultCnt);
//			if (isUserMessage) {
//				userMapper.discountMsgCntLimit(user);
//			}
			
			updateCnt++;
		}

		if (updateCnt > 0)
		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.core.service.MessageSendService#sendCallback(java.lang.String, int)
	 */
	@Override
	public void sendCallback(String serverId, int limit) {
		// TODO Auto-generated method stub
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("keyMon", this.getCallbackTableName(serverId));
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<AckCallback> list = ackMapper.getCallbackList(param);
		// 1. ack 조회
		for (AckCallback ack : list) {
			// 2. callback
			if (ack.getCallbackUrl() != null
					&& ack.getCallbackUrl().trim().length() > 0) {
				// 3. RestTemplate
				logger.info("ack.getCallbackUrl() {}", ack.getCallbackUrl());
				logger.info("ack.getAckType() {}",	ack.getAckType());
				logger.info("ack.getApplicationKey() {}",	ack.getApplicationKey());
				if (ack.getCallbackMethod().equals("POST")) {
					try {
						String result = restTemplate.postForObject(
								ack.getCallbackUrl(), this.getRequest(ack),
								String.class);
						logger.info("{}", result);
						ack.setCallbackStatus(1);
						ack.setCallbackCount(1);
					} catch (Exception e) {
						e.printStackTrace();
						// error
						ack.setCallbackStatus(-1);
						ack.setCallbackCount(1);

					}
				}

			} else {
				ack.setCallbackStatus(1);
				ack.setCallbackCount(0);
			}

			logger.info("updateAckCallback before {}", ack.getMsgId());
			// 4. ack update
			ackMapper.updateAckCallback(this.getParams(ack));
			logger.info("updateAckCallback after {}", ack.getMsgId());
			// 5. ctl_q 삭제
			ctlQMapper.deleteQ(ack.getMsgId());
			logger.info("deleteQ end {}", ack.getMsgId());

		}

	}

	/**
	 * Gets the request.
	 *
	 * @param ack the ack
	 * @return the request
	 */
	private Object getRequest(AckCallback ack) {

		HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(new MediaType("application", "json",
		// Charset.forName("UTF-8")));
		headers.set(StaticConfig.HEADER_APPLICATION_KEY,
				ack.getApplicationKey());

		CallbackReq req = new CallbackReq();
		req.setCallbackmsgid(ack.getMsgId());
		req.setAcktype(ack.getAckType());
		req.setAcktime(DateUtil.getDate(ack.getAckTime()));
		req.setAckresult(true);
		HttpEntity entity = new HttpEntity(req, headers);

		return entity;
	}

	/**
	 * Gets the params.
	 *
	 * @param ack the ack
	 * @return the params
	 */
	private Map<String, Object> getParams(AckCallback ack) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("keyMon", this.getKeyMon(ack.getMsgId()));
		params.put("callbackStatus", ack.getCallbackStatus());
		params.put("callbackCount", ack.getCallbackCount());
		params.put("ackType", ack.getAckType());
		params.put("issueId", ack.getIssueId());
		params.put("msgId", ack.getMsgId());
		params.put("tokenId", ack.getTokenId());

		return params;
	}

}
