package kr.co.adflow.pms.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.CtlQ;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.mapper.ValidationMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.svc.service.PushMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSendServiceImpl implements MessageSendService {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendServiceImpl.class);

	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PushMessageService pushMessageService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ValidationMapper validationMapper;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private CtlQMapper ctlQMapper;

	@Override
	public int sendMessageArray(String serverId, int limit) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		// param.put("keyMon", DateUtil.getYYYYMM());
		param.put("keyMon", this.getMessageTableName(serverId));
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectList(param);

		logger.info("list cnt :: {}", list.size());

		int updateCnt = 0;
		for (Message msg : list) {

			msg.setKeyMon(this.getKeyMon(msg.getMsgId()));

			logger.info("msg.getMsgId() {}", msg.getMsgId());
			logger.info("msg.getReceiverTopic() {}", msg.getReceiverTopic());
			logger.info("msg.getReceiver() {}", msg.getReceiver());

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
					msg.setStatus(PmsConfig.MESSAGE_STATUS_RECEIVER_NOT_FOUNT);
					messageMapper.updateStatus(msg);
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
				msg.setStatus(PmsConfig.MESSAGE_STATUS_COUNT_OVER);
				messageMapper.updateStatus(msg);
				continue;
			}

			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(
					msg));

			// 재전송 로직 추가
			String msgId = msg.getMsgId();
			if (msg.getResendMaxCount() > 0) {

				long reservationTime = System.currentTimeMillis();
				int resendCnt = 2;
				for (int i = 0; i < msg.getResendMaxCount(); i++) {
					int intervalM = msg.getResendInterval();
					msg.setReservation(true);
					long interval = intervalM * 1000 * 60;
					reservationTime = reservationTime + interval;
					msg.setReservationTime(new Date(reservationTime));
					msg.setMsgId(this.getMsgId());
					msg.setResendCount(resendCnt++);
					msg.setResendId(msgId);
					messageMapper.insertMessage(msg);
					messageMapper.insertContent(msg);

					ctlQMapper.insertQ(this.getCtlQ(msg));

				}
			}
			msg.setMsgId(msgId);
			// msg.getReceiverTopic();
			msg.setStatus(PmsConfig.MESSAGE_STATUS_SEND);

			int resultCnt = messageMapper.updateStatus(msg);
			ctlQMapper.deleteQ(msg.getMsgId());
			logger.info("update count is {}", resultCnt);
			// message_cnt - result_count
			User user = new User();
			user.setUserId(msg.getIssueId());
			user.setMsgCntLimit(resultCnt);
			if (isUserMessage) {
				userMapper.discountMsgCntLimit(user);
			}
			updateCnt++;
		}

		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}

	private String getMessageTableName(String serverId) {
		CtlQ result = null;
		CtlQ paramCtlQ = new CtlQ();
		paramCtlQ.setExeType(PmsConfig.CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE);
		paramCtlQ.setServerId(serverId);
		result = ctlQMapper.fetchQ(paramCtlQ);

		if (result == null) {
			return DateUtil.getYYYYMM();
		} else {
			return result.getTableName();
		}
	}

	private boolean validReceiverUserId(String receiver) {

		boolean result = false;

		int type = userValidator.getRequestType(receiver);

		if (PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_PHONE == type) {
			result = validationMapper.validPhoneNo(userValidator
					.getRegstPhoneNo(receiver));
		}

		if (PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1 == type
				|| PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2 == type) {
			result = validationMapper.validUfmiNo(userValidator
					.getRegstUfmiNo(receiver));
		}

		return result;

	}

	private CtlQ getCtlQ(Message msg) {
		CtlQ ctlQ = new CtlQ();

		if (msg.isReservation()) {
			ctlQ.setExeType(PmsConfig.CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION);
			ctlQ.setIssueTime(msg.getReservationTime());
		} else {
			ctlQ.setExeType(PmsConfig.CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE);
			ctlQ.setIssueTime(new Date());
		}

		ctlQ.setTableName(msg.getKeyMon());
		ctlQ.setMsgId(msg.getMsgId());
		ctlQ.setServerId(msg.getServerId());

		return ctlQ;
	}

	private boolean isPhoneNo(String receiver) {
		if ("010".equals(receiver.substring(0, 3))) {
			return true;
		} else {
			return false;
		}

	}

	private String getKeyMon(String string) {
		String result = string.substring(0, 6);
		logger.debug("getKeyMon is {}", result);
		return result;
	}

	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

	private String getReceiverTopic(String receiver) {

		String result = null;

		int type = userValidator.getRequestType(receiver);

		if (PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_PHONE == type) {
			result = userValidator.getSubscribPhoneNo(receiver);
		}

		if (PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1 == type) {
			result = userValidator.getSubscribUfmi1(receiver);
		}

		if (PmsConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2 == type) {
			result = userValidator.getSubscribUfmi2(receiver);
		}

		return result;
	}

	private String getReservationTableName(String serverId) {
		CtlQ result = null;
		CtlQ paramCtlQ = new CtlQ();
		paramCtlQ.setExeType(PmsConfig.CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION);
		paramCtlQ.setServerId(serverId);
		result = ctlQMapper.fetchQ(paramCtlQ);

		if (result == null) {
			return DateUtil.getYYYYMM();
		} else {
			return result.getTableName();
		}
	}

	@Override
	public int sendReservationMessageArray(String serverId, int limit) {
		HashMap<String, Object> param = new HashMap<String, Object>();

		// param.put("keyMon", DateUtil.getYYYYMM());
		param.put("keyMon", this.getReservationTableName(serverId));
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
					msg.setStatus(-2);
					messageMapper.updateStatus(msg);
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
				msg.setStatus(PmsConfig.MESSAGE_STATUS_COUNT_OVER);
				messageMapper.updateStatus(msg);
				continue;
			}

			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(
					msg));

			msg.setStatus(PmsConfig.MESSAGE_STATUS_SEND);

			int resultCnt = messageMapper.updateStatus(msg);
			ctlQMapper.deleteQ(msg.getMsgId());
			logger.info("update count is {}", resultCnt);
			// message_cnt - result_count
			User user = new User();
			user.setUserId(msg.getIssueId());
			user.setMsgCntLimit(resultCnt);
			if (isUserMessage) {
				userMapper.discountMsgCntLimit(user);
			}
			updateCnt++;
		}

		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}

}
