/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.CtlQ;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class SvcAdmServiceImpl.
 */
@Service
public class SvcAdmServiceImpl implements SvcAdmService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(SvcAdmServiceImpl.class);

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

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
	 * kr.co.adflow.pms.adm.service.SvcAdmService#sendMessage(java.lang.String,
	 * kr.co.adflow.pms.adm.request.MessageReq)
	 */
	@Override
	public List<Map<String, String>> sendMessage(String appKey, MessageReq message) {
		// String[] msgIdArray = null;

		List<Map<String, String>> resultList = null;

		// 1. get userId by appKey
		String issueId = interceptMapper.selectCashedUserId(appKey);

		// if (message.getContent().getBytes().length > this
		// .getMessageSizeLimit(issueId)) {
		// throw new RuntimeException(" message body size limit over :"
		// + this.getMessageSizeLimit(issueId));
		// }
		// 2. get max count by userId
		// 3. check max count
		// if (userMapper.getMsgCntLimit(issueId) <
		// message.getReceivers().length) {
		// throw new
		// RuntimeException("send message count is message count limit over ");
		// }
		// svcadm svc 갯수 제한 없음

		Message msg = new Message();
		msg.setKeyMon(DateUtil.getYYYYMM());

		msg.setServerId(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID);

		if (message.getMsgType() == 0) {
			msg.setMsgType(pmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
		} else {
			msg.setMsgType(message.getMsgType());
		}

		if (message.getExpiry() == 0) {
			msg.setExpiry(this.getMessageExpiry(issueId));
		} else {
			msg.setExpiry(message.getExpiry());
		}

		if (message.getQos() == 0) {
			msg.setQos(this.getMessageQos(issueId));
		} else {
			msg.setQos(message.getQos());
		}

		msg.setIssueId(issueId);
		msg.setUpdateId(issueId);

		if (message.getServiceId() == null || message.getServiceId().trim().length() == 0) {
			msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		} else {
			msg.setServiceId(message.getServiceId());
		}

		msg.setAck(message.isAck());

		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		msg.setFileFormat(message.getFileFormat());
		msg.setFileName(message.getFileName());

		if (message.getReservationTime() != null) {
			msg.setReservationTime(message.getReservationTime());
			msg.setReservation(true);
		} else {
			msg.setReservation(false);
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

		// admin message
		// for (int i = 0; i < receivers.length; i++) {
		// if (!userValidator.validRequestValue(receivers[i])) {
		// throw new RuntimeException("receivers formatting error count : " +
		// i);
		// }
		// }

		// User Message false = 0
		msg.setRetained(0);

		resultList = new ArrayList<Map<String, String>>(receivers.length);

		Map<String, String> msgMap = null;
		String groupId = null;
		for (int i = 0; i < receivers.length; i++) {

			msgMap = new HashMap<String, String>();
			msg.setReceiver(receivers[i]);
			msg.setReceiverTopic(receivers[i]);
			msg.setMsgId(this.getMsgId());

			// // MEMO 여러 건일때 0 번째 msgId 를 대표로 groupId에 추가
			// if (i == 0) {
			// groupId = msg.getMsgId();
			// }
			// msg.setGroupId(groupId);

			// group topic check
			if (receivers[i].subSequence(0, 5).equals("mms/P") && receivers[i].indexOf("g") > 0) {
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

	/**
	 * Gets the message size limit.
	 *
	 * @param userId
	 *            the user id
	 * @return the message size limit
	 */
	private int getMessageSizeLimit(String userId) {
		return checkUtil.getMessageSizeLimit(interceptMapper.getCashedMessageSizeLimit(userId));
	}

	/**
	 * Gets the message expiry.
	 *
	 * @param userId
	 *            the user id
	 * @return the message expiry
	 */
	private int getMessageExpiry(String userId) {
		return checkUtil.getMessageExpiry(interceptMapper.getCashedMessageExpiry(userId));
	}

	/**
	 * Gets the message qos.
	 *
	 * @param userId
	 *            the user id
	 * @return the message qos
	 */
	private int getMessageQos(String userId) {
		return checkUtil.getMessageQos(interceptMapper.getCashedMessageQos(userId));
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

}
