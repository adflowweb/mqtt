/*
 * 
 */
package kr.co.adflow.pms.users.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.resource.spi.ConnectionManager;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;
import kr.co.adflow.pms.core.handler.PCFConnectionManagerHandler;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.core.util.RecordFomatUtil;
import kr.co.adflow.pms.core.util.MessageTRLog;
import kr.co.adflow.pms.domain.GroupMessage;
//import kr.co.adflow.pms.core.handler.PCFConnectionManager;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.push.mapper.PushMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.users.request.MessageReq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

// TODO: Auto-generated Javadoc
/**
 * The Class UserMessageServiceImpl.
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserMessageServiceImpl.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The push Mapper. */
	@Autowired
	private PushMapper pushMapper;

	/** The check util. */
	@Autowired
	private CheckUtil checkUtil;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/** The jms template. */
	@Autowired
	private JmsTemplate jmsTemplate;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.UserMessageService#sendMessage(java.lang.
	 * String, kr.co.adflow.pms.svc.request.MessageReq)
	 */
	@Override
	public int sendMessage(MessageReq message, String appKey) throws Exception {

		logger.debug("=== message::{}",
				"getContentType::" + message.getContentType() + ",getExpiry::" + message.getExpiry() + ",getQos::"
						+ message.getQos() + ",getReceiver::" + message.getReceiver() + ",getSender::"
						+ message.getSender());
		// String[] msgIdArray = null;

		int resultCnt = 0;

		// 1. get userId by appKey
		String issueId = message.getSender();

		// if (message.getContent().getBytes().length > this
		// .getMessageSizeLimit(issueId)) {
		// throw new RuntimeException(" message body size limit over :"
		// + this.getMessageSizeLimit(issueId));
		// }
		// 2. get max count by userId
		// 3. check max count

		Message msg = new Message();
		msg.setKeyMon(DateUtil.getYYYYMM());

		msg.setServerId("none");

		// Expiry 600 sec fix
		// msg.setExpiry(message.getExpiry());
		msg.setExpiry(pmsConfig.MESSAGE_USER_MESSAGE_EXPIRY_DEFAULT);

		msg.setQos(message.getQos());
		msg.setIssueId(issueId);
		msg.setUpdateId(issueId);
		msg.setIssueName(issueId);

		msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_USERMESSAGE);
		// Ack false
		msg.setAck(true);
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());

		msg.setMsgId(this.getMsgId());

		// user message => 106
		msg.setMsgType(106);

		// message size
		if (message.getContentLength() == null) {
			message.setContentLength(0);
		}
		msg.setMsgSize(message.getContentLength());

		// logger.info(" ========= getMsgSize ::{}",msg.getMsgSize());

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

		// WEB:0, P-Talk1.0:1, P-Talk2.0:2
		if (msg.getIssueId().subSequence(5, 6).equals("1")) {
			msg.setSendTerminalType(1);
		} else {
			msg.setSendTerminalType(2);
		}

		msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);

		// sendJMS
		msg.setReceiver(message.getReceiver());
		msg.setReceiverTopic(message.getReceiver());
		// Group Message check;
		if (message.getReceiver().indexOf("g") > 0) {
			// group message Ok
			msg.setGroupId(message.getReceiver());
		}

		this.sendJMS(msg);
		resultCnt++;

		// //Group Message check;
		// String groupTemp = message.getReceiver();
		// if (groupTemp.indexOf("g") > 0) {
		// //group message Ok
		// List<GroupMessage> list = this.groupList(groupTemp, msg.getKeyMon(),
		// msg.getMsgId() , appKey);
		//
		// for (GroupMessage groupMessage : list) {
		// //insert Group message
		// messageMapper.insertGroupMessage(groupMessage);
		// resultCnt++;
		// }
		//
		// } else {
		// GroupMessage groupMessage = new GroupMessage();
		// groupMessage.setKeyMon(msg.getKeyMon());
		// groupMessage.setMsgId(msg.getMsgId());
		//
		// String ufmi = RecordFomatUtil.topicToUfmi(msg.getReceiverTopic());
		//
		// String token = pushMapper.getToken(ufmi);
		// if(token != null){
		// groupMessage.setReceiverTokenId(token);
		// }else{
		// groupMessage.setReceiverTokenId("");
		// }
		// messageMapper.insertGroupMessage(groupMessage);
		// }

		return resultCnt;
	}

	// JMS Send and Message DB insert
	public void sendJMS(Message msg) {
		logger.debug("=== msg::{}",
				"getContentType::" + msg.getContentType() + ",getExpiry::" + msg.getExpiry() + ",getQos::"
						+ msg.getQos() + ",getReceiver::" + msg.getReceiver() + ",getIssueId::" + msg.getIssueId()
						+ ",getAppAckType::" + msg.getAppAckType() + ",getGroupId::" + msg.getGroupId()
						+ ",getIssueName::" + msg.getIssueName() + ",getKeyMon::" + msg.getKeyMon() + ",getMediaType::"
						+ msg.getMediaType() + ",getMsgId::" + msg.getMsgId() + ",getMsgSize::" + msg.getMsgSize()
						+ ",getMsgType::" + msg.getMsgType() + ",getPmaAckType::" + msg.getPmaAckType()
						+ ",getReceiverTopic::" + msg.getReceiverTopic() + ",getResendId::" + msg.getResendId()
						+ ",getResendMaxCount::" + msg.getResendMaxCount() + ",getRetained::" + msg.getRetained()
						+ ",getSendTerminalType::" + msg.getSendTerminalType() + ",getServerId::" + msg.getServerId()
						+ ",getServiceId::" + msg.getServiceId() + ",getStatus::" + msg.getStatus() + ",getUpdateId::"
						+ msg.getUpdateId() + ",getAppAckTime::" + msg.getAppAckTime() + ",getPmaAckTime::"
						+ msg.getIssueTime() + ",getPmaAckTime::" + msg.getPmaAckTime() + ",getReservationTime::"
						+ msg.getReservationTime() + ",getUpdateTime::" + msg.getUpdateTime());
		// JMS message send
		jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate, msg));

		// message tran log
		try {
			MessageTRLog.log(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// DB message insert
		messageMapper.insertMessage(msg);
		messageMapper.insertContent(msg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.UserMessageService#sendMessage(java.lang.
	 * String, kr.co.adflow.pms.svc.request.MessageReq)
	 */
	@Override
	public Integer groupListCnt(String groupTopic) throws Exception {

		logger.debug("=== groupTopic::{}", groupTopic);
		// PCFConnectionManagerHandler.PCFConnectionManager();

		Integer resultCnt = null;
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {
			qmgr = new MQQueueManager("*", connMan);

			// PCFMessageAgent agent = new PCFMessageAgent(qmgr);
			// PCFMessage request = new
			// PCFMessage(MQConstants.MQCMD_INQUIRE_SUB_STATUS);
			// request.addParameter(MQConstants.MQCACF_SUB_NAME, "*");
			//
			//
			// request.addFilterParameter(MQConstants.MQCA_TOPIC_STRING,MQConstants.MQCFOP_EQUAL,
			// groupTopic);
			//
			// long start;
			// long stop;
			// start = System.currentTimeMillis();
			// PCFMessage[] responses;
			// responses = agent.send(request);
			//
			// stop = System.currentTimeMillis();
			// System.out.println("elapsedTime=" + (stop - start) + "ms");
			// System.out.println("responses.length=" + responses.length);
			//
			// String token;
			// int point = 0;
			// for (int i = 0; i < responses.length; i++) {
			// token =
			// (String)responses[i].getParameterValue(MQConstants.MQCACF_SUB_NAME);
			// point = token.indexOf(":");
			// token = token.substring(0, token.indexOf(":"));
			// System.out.println(i+":"+token);
			// System.out.println("ufmi ::"+pushMapper.getUfmi(token));
			// }
			//
			//
			// resultCnt = responses.length;

			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_TOPIC_STATUS);
			request.addParameter(MQConstants.MQCA_TOPIC_STRING, groupTopic);

			long start;
			long stop;
			start = System.currentTimeMillis();
			PCFMessage[] responses;
			responses = agent.send(request);

			stop = System.currentTimeMillis();
			logger.debug("elapsedTime=" + (stop - start) + "ms");
			logger.debug("responses.length=" + responses.length);

			resultCnt = (Integer) responses[0].getParameterValue(MQConstants.MQIA_SUB_COUNT);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (agent != null) {
				try {
					agent.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}

		}

		return resultCnt;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.UserMessageService#sendMessage(java.lang.
	 * String, kr.co.adflow.pms.svc.request.MessageReq)
	 */
	public List<GroupMessage> groupList(String groupTopic, String keyMon, String msgId, String appKey)
			throws Exception {

		logger.debug("=== parm::{}",
				"groupTopic::" + groupTopic + ",keyMon::" + keyMon + ",msgId::" + msgId + ",appKey::" + appKey);

		// PCFConnectionManagerHandler.PCFConnectionManager();

		int resultCnt = 0;
		List<GroupMessage> list = new ArrayList<GroupMessage>();
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {
			qmgr = new MQQueueManager("*", connMan);

			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_SUB_STATUS);
			request.addParameter(MQConstants.MQCACF_SUB_NAME, "*");

			request.addFilterParameter(MQConstants.MQCA_TOPIC_STRING, MQConstants.MQCFOP_EQUAL, groupTopic);

			long start;
			long stop;
			start = System.currentTimeMillis();
			PCFMessage[] responses;
			responses = agent.send(request);

			stop = System.currentTimeMillis();
			logger.debug("elapsedTime=" + (stop - start) + "ms");
			logger.debug("responses.length=" + responses.length);

			String token;
			String ufmi;
			int point = 0;
			for (int i = 0; i < responses.length; i++) {
				GroupMessage groupMessage = new GroupMessage();
				groupMessage.setKeyMon(keyMon);
				groupMessage.setMsgId(msgId);
				token = (String) responses[i].getParameterValue(MQConstants.MQCACF_SUB_NAME);
				point = token.indexOf(":");
				token = token.substring(0, token.indexOf(":"));
				groupMessage.setReceiverTokenId(token);

				logger.debug(i + ":" + token);
				ufmi = pushMapper.getUfmi(token);
				groupMessage.setReceiverUfmi(ufmi);

				logger.debug("=== groupMessage::{}",
						"getKeyMon::" + groupMessage.getKeyMon() + ",getMsgId::" + groupMessage.getMsgId()
								+ ",getReceiverTokenId::" + groupMessage.getReceiverTokenId() + ",getReceiverUfmi::"
								+ groupMessage.getReceiverUfmi());
				if (ufmi != null && !token.equals(appKey)) {
					list.add(groupMessage);
				} else {
					logger.info("Group message self skip - token ::{} appkey ::{}", token, appKey);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;

		} catch (MQException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (agent != null) {
				try {
					agent.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}

		}

		return list;
	}

	private String getServerId() {
		// pmsConfig.EXECUTOR_SERVER_ID

		String result = null;

		// long mod = System.nanoTime() % 3;
		// if (mod == 0) {
		// result = pmsConfig.EXECUTOR_SERVER_ID1;
		// } else if (mod == 1) {
		// result = pmsConfig.EXECUTOR_SERVER_ID2;
		// } else if (mod == 2) {
		// result = pmsConfig.EXECUTOR_SERVER_ID3;
		// }
		result = pmsConfig.EXECUTOR_SERVER_ID;
		logger.debug("=== result::{}", result);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.svc.service.UserMessageService#getMessageResult(kr.co.
	 * adflow.pms.svc.request.MessageIdsReq, java.lang.String)
	 */

	/**
	 * Gets the receiver topic.
	 *
	 * @param receiver
	 *            the receiver
	 * @return the receiver topic
	 */
	private String getReceiverTopic(String receiver) {

		logger.debug("=== receiver::{}", receiver);

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

		logger.debug("=== result::{}", result);
		return result;
	}

}
