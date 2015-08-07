/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AddressMessageReq;
import kr.co.adflow.pms.adm.request.MessageIdsReq;
import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.AddressMessage;
import kr.co.adflow.pms.domain.CtlQ;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class PushMessageServiceImpl.
 */
@Service
public class PushMessageServiceImpl implements PushMessageService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageServiceImpl.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/** The validation mapper. */
	// @Autowired
	// private ValidationMapper validationMapper;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	/** The ctl q mapper. */
	@Autowired
	private CtlQMapper ctlQMapper;

	/** The check util. */
	@Autowired
	private CheckUtil checkUtil;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#sendMessage(java.lang
	 * .String, kr.co.adflow.pms.svc.request.MessageReq)
	 */
	@Override
	public List<Map<String, String>> sendMessage(String appKey,
			MessageReq message) throws Exception {
		logger.debug(
				"=== appKey::{}, MessageReq={}",
				appKey,
				"getContentType::" + message.getContentType()
						+ ",getResendInterval::" + message.getResendInterval()
						+ ",getResendMaxCount::" + message.getResendMaxCount()
						+ ",getReservationTime::"
						+ message.getReservationTime());

		// String[] msgIdArray = null;

		List<Map<String, String>> resultList = null;

		// 1. get userId by appKey
		String issueId = interceptMapper.selectCashedUserId(appKey);

		// message size limit skip
		// if (message.getContent().getBytes().length > this
		// .getMessageSizeLimit(issueId)) {
		// throw new RuntimeException(" message body size limit over :"
		// + this.getMessageSizeLimit(issueId));
		// throw new PmsRuntimeException("invalid auth");
		//
		// }
		// 2. get max count by userId
		// 3. check max count
		// msgCntLimit disable
		// if (userMapper.getMsgCntLimit(issueId) <
		// message.getReceivers().length) {
		// throw new RuntimeException(
		// "send message count is message count limit over ");
		// }

		Message msg = new Message();
		msg.setKeyMon(DateUtil.getYYYYMM());

		msg.setServerId(this.getServerId());
		msg.setMsgType(pmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
		msg.setExpiry(this.getMessageExpiry(issueId));
		msg.setQos(this.getMessageQos(issueId));
		msg.setIssueId(issueId);
		msg.setUpdateId(issueId);

		msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		msg.setAck(pmsConfig.MESSAGE_ACK_DEFAULT);
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		msg.setFileFormat(message.getFileFormat());
		msg.setFileName(message.getFileName());

		if (message.getReservationTime() != null) {
			msg.setReservationTime(message.getReservationTime());
			msg.setReservation(true);
		}

		msg.setResendMaxCount(message.getResendMaxCount());
		msg.setResendInterval(message.getResendInterval());

		// WEB:0, P-Talk1.0:1, P-Talk2.0:2
		msg.setSendTerminalType(0);

		// message size
		if (message.getContentLength() == null) {
			message.setContentLength(0);
		}
		msg.setMsgSize(message.getContentLength());

		// TMS:0, MMS:1
		if (message.isMms()) {
			msg.setMediaType(1);
		} else {
			if (msg.getMsgSize() > 140) {
				msg.setMediaType(1);
			} else {
				msg.setMediaType(0);
			}

		}

		String[] receivers = message.getReceivers();

		// msgIdArray = new String[receivers.length];

		for (int i = 0; i < receivers.length; i++) {
			logger.debug("=== receivers[{}]::{}", i, receivers[i]);

			// group topic check
			if (!(receivers[i].subSequence(0, 5).equals("mms/P") && receivers[i]
					.indexOf("g") > 0)) {

				if (!userValidator.validRequestValue(receivers[i])) {
					// throw new RuntimeException(
					// "receivers formatting error count : " + i);
					throw new PmsRuntimeException(
							"receivers formatting error count : " + i);
				}

			}

		}

		resultList = new ArrayList<Map<String, String>>(receivers.length);

		String groupId = null;
		Map<String, String> msgMap = null;
		for (int i = 0; i < receivers.length; i++) {

			msgMap = new HashMap<String, String>();
			msg.setReceiver(receivers[i]);
			msg.setMsgId(this.getMsgId());
			// // MEMO 여러 건일때 0 번째 msgId 를 대표로 groupId에 추가
			// if (i == 0) {
			// groupId = msg.getMsgId();
			// }
			// msg.setGroupId(groupId);

			// group topic check
			if (receivers[i].subSequence(0, 5).equals("mms/P")
					&& receivers[i].indexOf("g") > 0) {
				msg.setGroupId(receivers[i]);
				msg.setReceiverTopic(receivers[i]);
			}

			// MEMO resend msg 인 경우 resendId 에 msgId 추가
			if (msg.getResendCount() > 0) {
				msg.setResendId(msg.getMsgId());
			} else {
				msg.setResendId(null);
			}

			msg.setStatus(StaticConfig.MESSAGE_STATUS_SENDING);

			// reservation message check
			if (msg.isReservation()) {
				messageMapper.insertReservationMessage(msg);
			} else {
				messageMapper.insertMessage(msg);
				messageMapper.insertContent(msg);
				ctlQMapper.insertQ(this.getCtlQ(msg));
			}

			msgMap.put("msgId", msg.getMsgId());
			msgMap.put("receiver", msg.getReceiver());

			resultList.add(msgMap);
			logger.info("message id :: {}", msg.getMsgId());
		}

		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#sendMessage(java.lang
	 * .String, kr.co.adflow.pms.svc.request.MessageReq)
	 */
	@Override
	public List<Map<String, String>> sendAddressMessage(String appKey,
			AddressMessageReq addressMsg) throws Exception {

		// String[] msgIdArray = null;

		List<Map<String, String>> resultList = null;

		// 1. get userId by appKey
		String issueId = interceptMapper.selectCashedUserId(appKey);

		Message msg = new Message();
		msg.setKeyMon(DateUtil.getYYYYMM());

		msg.setServerId(this.getServerId());
		msg.setMsgType(pmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
		msg.setExpiry(this.getMessageExpiry(issueId));
		msg.setQos(this.getMessageQos(issueId));
		msg.setIssueId(issueId);
		msg.setUpdateId(issueId);

		msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		msg.setAck(pmsConfig.MESSAGE_ACK_DEFAULT);
		msg.setContentType(addressMsg.getContentType());
		msg.setFileFormat(addressMsg.getFileFormat());
		msg.setFileName(addressMsg.getFileName());

		if (addressMsg.getReservationTime() != null) {
			msg.setReservationTime(addressMsg.getReservationTime());
			msg.setReservation(true);
		}

		msg.setResendMaxCount(addressMsg.getResendMaxCount());
		msg.setResendInterval(addressMsg.getResendInterval());

		// WEB:0, P-Talk1.0:1, P-Talk2.0:2
		msg.setSendTerminalType(0);

		for (AddressMessage addressMessage : addressMsg
				.getAddressMessageArray()) {
			// group topic check
			if (!(addressMessage.getReceiver().subSequence(0, 5)
					.equals("mms/P") && addressMessage.getReceiver().indexOf(
					"g") > 0)) {

				if (!userValidator.validRequestValue(addressMessage
						.getReceiver())) {
					// throw new RuntimeException(
					// "receivers formatting error count : " + i);
					throw new PmsRuntimeException(
							"receivers formatting error : "
									+ addressMessage.getReceiver());
				}

			}
		}

		resultList = new ArrayList<Map<String, String>>(
				addressMsg.getAddressMessageArray().length);

		String groupId = null;
		Map<String, String> msgMap = null;

		for (AddressMessage adressMessage : addressMsg.getAddressMessageArray()) {
			msgMap = new HashMap<String, String>();
			msg.setReceiver(adressMessage.getReceiver());
			msg.setMsgId(this.getMsgId());
			msg.setContent(adressMessage.getContent());
			// message size
			if (adressMessage.getContentLength() == null) {
				adressMessage.setContentLength(0);
			}
			msg.setMsgSize(adressMessage.getContentLength());

			// TMS:0, MMS:1
			if (addressMsg.isMms()) {
				msg.setMediaType(1);
			} else {
				if (msg.getMsgSize() > 140) {
					msg.setMediaType(1);
				} else {
					msg.setMediaType(0);
				}

			}

			// // MEMO 여러 건일때 0 번째 msgId 를 대표로 groupId에 추가
			// if (i == 0) {
			// groupId = msg.getMsgId();
			// }
			// msg.setGroupId(groupId);

			// group topic check
			if (adressMessage.getReceiver().subSequence(0, 5).equals("mms/P")
					&& adressMessage.getReceiver().indexOf("g") > 0) {
				msg.setGroupId(adressMessage.getReceiver());
				msg.setReceiverTopic(adressMessage.getReceiver());
			}

			// MEMO resend msg 인 경우 resendId 에 msgId 추가
			if (msg.getResendCount() > 0) {
				msg.setResendId(msg.getMsgId());
			} else {
				msg.setResendId(null);
			}

			msg.setStatus(StaticConfig.MESSAGE_STATUS_SENDING);

			// reservation message check
			if (msg.isReservation()) {
				messageMapper.insertReservationMessage(msg);
			} else {
				messageMapper.insertMessage(msg);
				messageMapper.insertContent(msg);
				ctlQMapper.insertQ(this.getCtlQ(msg));
			}

			msgMap.put("msgId", msg.getMsgId());
			msgMap.put("receiver", msg.getReceiver());

			resultList.add(msgMap);
			logger.info("message id :: {}", msg.getMsgId());
		}

		return resultList;
	}

	private String getServerId() {
		// pmsConfig.EXECUTOR_SERVER_ID

		String result = null;

		long mod = System.nanoTime() % 3;
		if (mod == 0) {
			result = pmsConfig.EXECUTOR_SERVER_ID1;
		} else if (mod == 1) {
			result = pmsConfig.EXECUTOR_SERVER_ID2;
		} else if (mod == 2) {
			result = pmsConfig.EXECUTOR_SERVER_ID3;
		}

		// else if (mod == 3) {
		// result = pmsConfig.EXECUTOR_SERVER_ID4;
		// } else if (mod == 4) {
		// result = pmsConfig.EXECUTOR_SERVER_ID5;
		// } else {
		// result = pmsConfig.EXECUTOR_SERVER_ID6;
		// }

		return result;
	}

	/**
	 * Gets the ctl q.
	 * 
	 * @param msg
	 *            the msg
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

	/**
	 * Gets the msg id.
	 * 
	 * @return the msg id
	 */
	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#getMessageResult(kr.co
	 * .adflow.pms.svc.request.MessageIdsReq, java.lang.String)
	 */
	@Override
	public List<MessageResult> getMessageResult(MessageIdsReq msgIds,
			String appKey) throws Exception {

		List<MessageResult> resultList = null;

		String issueId = interceptMapper.selectCashedUserId(appKey);

		MsgIdsParams param = new MsgIdsParams();

		param.setKeyMon(this.getKeyMon(msgIds.getMsgIds()));
		param.setIssueId(issueId);
		param.setMsgIds(msgIds.getMsgIds());

		List<Message> list = messageMapper.getMessageResult(param);

		resultList = new ArrayList<MessageResult>();
		MessageResult messageResult = null;
		for (Message msg : list) {
			messageResult = new MessageResult();

			messageResult.setMsgId(msg.getMsgId());
			messageResult.setReceiver(msg.getReceiver());
			messageResult.setResendCount(msg.getResendCount());
			messageResult.setReservation(msg.isReservation());
			messageResult.setReservationTime(msg.getReservationTime());
			messageResult.setStatus(msg.getStatus());
			messageResult.setUpdateTime(msg.getUpdateTime());
			messageResult.setPmaAckType(msg.getPmaAckType());
			messageResult.setPmaAckTime(msg.getPmaAckTime());
			messageResult.setAppAckType(msg.getAppAckType());
			messageResult.setAppAckTime(msg.getAppAckTime());
			messageResult.setUfmi(msg.getUfmi());

			resultList.add(messageResult);
		}

		return resultList;
	}

	/**
	 * Gets the key mon.
	 * 
	 * @param msgIds
	 *            the msg ids
	 * @return the key mon
	 */
	private String getKeyMon(String[] msgIds) throws Exception {
		if (msgIds.length < 1) {
			// throw new RuntimeException("msgId not found");
			throw new PmsRuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#validPhoneNo(java.lang
	 * .String)
	 */
	@Override
	public Boolean validPhoneNo(String phoneNo) {
		return false;
		// return validationMapper.validPhoneNo(this.getPushUserId(phoneNo));
	}

	/**
	 * Gets the push user id.
	 * 
	 * @param phoneNo
	 *            the phone no
	 * @return the push user id
	 */
	private String getPushUserId(String phoneNo) {
		return userValidator.getRegstPhoneNo(phoneNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#validUfmiNo(java.lang
	 * .String)
	 */
	@Override
	public Boolean validUfmiNo(String ufmiNo) {
		return false;
		// return validationMapper.validUfmiNo(this.getPushUfmi(ufmiNo));
	}

	/**
	 * Gets the push ufmi.
	 * 
	 * @param ufmiNo
	 *            the ufmi no
	 * @return the push ufmi
	 */
	private String getPushUfmi(String ufmiNo) {
		return userValidator.getRegstUfmiNo(ufmiNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.PushMessageService#cancelMessage(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public Integer cancelMessage(String appKey, String msgId) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		// Message msg = new Message();
		// msg.setKeyMon(this.getKeyMon(msgId));
		// msg.setMsgId(msgId);
		// msg.setUpdateId(issueId);
		// msg.setStatus(StaticConfig.MESSAGE_STATUS_RESEVATION_CANCEL);

		Message msg;
		String keyMon = DateUtil.getYYYYMM();
		List<Message> list = messageMapper.selectReservationMessage(msgId);
		msg = list.get(0);
		// msg = messageMapper.selectReservationMessage(msgId);
		msg.setKeyMon(keyMon);
		msg.setStatus(StaticConfig.MESSAGE_STATUS_RESEVATION_CANCEL);
		int cnt = messageMapper.insertMessageRV(msg);
		messageMapper.insertContent(msg);
		messageMapper.deleteReservationMessage(msgId);

		// return messageMapper.cancelMessage(msg);
		return cnt;
	}

	/**
	 * Gets the key mon.
	 * 
	 * @param msgId
	 *            the msg id
	 * @return the key mon
	 */
	private String getKeyMon(String msgId) {
		// TODO Auto-generated method stub
		return msgId.substring(0, 6);
	}

	/**
	 * Gets the message size limit.
	 * 
	 * @param userId
	 *            the user id
	 * @return the message size limit
	 */
	private int getMessageSizeLimit(String userId) {
		return checkUtil.getMessageSizeLimit(interceptMapper
				.getCashedMessageSizeLimit(userId));
	}

	/**
	 * Gets the message expiry.
	 * 
	 * @param userId
	 *            the user id
	 * @return the message expiry
	 */
	private int getMessageExpiry(String userId) {
		return checkUtil.getMessageExpiry(interceptMapper
				.getCashedMessageExpiry(userId));
	}

	/**
	 * Gets the message qos.
	 * 
	 * @param userId
	 *            the user id
	 * @return the message qos
	 */
	private int getMessageQos(String userId) {
		return checkUtil.getMessageQos(interceptMapper
				.getCashedMessageQos(userId));
	}

	/**
	 * Gets the int.
	 * 
	 * @param string
	 *            the string
	 * @return the int
	 */
	private int getInt(String string) {
		return Integer.parseInt(string);
	}

	/**
	 * Gets the date.
	 * 
	 * @param string
	 *            the string
	 * @return the date
	 */
	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

}
