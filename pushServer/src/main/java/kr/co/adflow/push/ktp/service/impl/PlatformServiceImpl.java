/*
 * 
 */
package kr.co.adflow.push.ktp.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.jms.DeliveryMode;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.dao.TokenDao;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.domain.ktp.request.Ufmi;
import kr.co.adflow.push.domain.ktp.request.UserID;
import kr.co.adflow.push.domain.ktp.request.UserMessage;
import kr.co.adflow.push.ktp.handler.DirectMsgHandler;
import kr.co.adflow.push.ktp.handler.PreCheckHandler;
import kr.co.adflow.push.ktp.service.PlatformService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class PlatformServiceImpl.
 */
@Service
public class PlatformServiceImpl implements PlatformService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PlatformServiceImpl.class);

	/** The Constant CMD_KEEP_ALIVE_TIME. */
	private static final int CMD_KEEP_ALIVE_TIME = 102;

	/** The Constant CMD_PRE_CHECK. */
	private static final int CMD_PRE_CHECK = 103;

	/** The Constant CMD_FW_UPGRADE. */
	private static final int CMD_FW_UPGRADE = 104;

	/** The Constant CMD_PTT_UPDATE. */
	private static final int CMD_PTT_UPDATE = 105;

	/** The Constant CMD_USER_MESSAGE. */
	private static final int CMD_USER_MESSAGE = 10;

	/** The Constant DIG_ACCOUNT_INFO_INTENT_NAME. */
	private static final String DIG_ACCOUNT_INFO_ACTION_NAME = "kr.co.ktpowertel.push.digAccountInfo";

	/** The Constant DIG_USER_MESSAGE_INTENT_NAME. */
	private static final String DIG_USER_MESSAGE_ACTION_NAME = "kr.co.ktpowertel.push.userMessage";

	private static final String CONTENT_TYPE_JSON = "application/json";

	/** The jms template. */
	@Autowired
	private JmsTemplate jmsTemplate;

	/** The message dao. */
	@Resource
	MessageDao messageDao;

	/** The token dao. */
	@Resource
	TokenDao tokenDao;

	/** The Constant TIME_TO_LIVE. */
	private final static int TIME_TO_LIVE = 3000;

	// private @Value("#{code['precheck.time.to.live']}") int TIME_TO_LIVE;
	
	private int postMessage(Message message) {
		
		int cnt = 0;
		
		try {
			cnt = messageDao.post(message);
			if (cnt < 1) throw new Exception();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
			
		return cnt;	
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#sendPrecheck(java.lang.
	 * String)
	 */
	public void sendPrecheck(String topicName) {

		jmsTemplate.execute(topicName, new PreCheckHandler(TIME_TO_LIVE));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#modifyFwInfo(kr.co.adflow
	 * .push.domain.ktp.request.FwInfo)
	 */
	public void modifyFwInfo(FwInfo fwInfo) {

		Message message = new Message();

		message.setType(CMD_FW_UPGRADE);
		message.setQos(DeliveryMode.PERSISTENT);
		message.setStatus(Message.STATUS_WAIT_FOR_SENDING);
		message.setSender(fwInfo.getSender());
		message.setReceiver(fwInfo.getReceiver());
		message.setContentType(fwInfo.getContentType());
		message.setContent(fwInfo.getContent());

		try {
			int cnt = this.postMessage(message);

			jmsTemplate.execute(fwInfo.getReceiver(), new DirectMsgHandler(
					message));

			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.putIssue(message);

		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#modifyDigInfo(kr.co.adflow
	 * .push.domain.ktp.request.DigInfo)
	 */
	public void modifyDigInfo(DigInfo digInfo) {

		Message message = new Message();

		message.setType(CMD_PTT_UPDATE);
		message.setStatus(Message.STATUS_WAIT_FOR_SENDING);
		message.setQos(DeliveryMode.PERSISTENT);
		message.setServiceID(DIG_ACCOUNT_INFO_ACTION_NAME);

		message.setSender(digInfo.getSender());
		message.setReceiver(digInfo.getReceiver());
		message.setContentType(digInfo.getContentType());
		message.setContent(digInfo.getContent());

		try {

			int cnt = this.postMessage(message);
			
			jmsTemplate.execute(digInfo.getReceiver(), new DirectMsgHandler(
					message));

			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.putIssue(message);
		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#sendMessage(kr.co.adflow
	 * .push.domain.Message)
	 */
	public void sendMessage(Message message) {

		message.setStatus(Message.STATUS_WAIT_FOR_SENDING);
		message.setIssue(new Date());

		try {
			int cnt = this.postMessage(message);

			jmsTemplate.execute(message.getReceiver(), new DirectMsgHandler(
					message));

			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.putIssue(message);
		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#modifyKeepAliveTime(kr.
	 * co.adflow.push.domain.ktp.request.KeepAliveTime)
	 */
	public void modifyKeepAliveTime(KeepAliveTime keepAliveTime) {

		Message message = new Message();

		message.setType(CMD_KEEP_ALIVE_TIME);
		message.setStatus(Message.STATUS_WAIT_FOR_SENDING);
		message.setQos(DeliveryMode.PERSISTENT);
		message.setIssue(new Date());

		message.setSender(keepAliveTime.getSender());
		message.setReceiver(keepAliveTime.getReceiver());
		message.setContentType(CONTENT_TYPE_JSON);
		message.setContent(keepAliveTime.getContent());

		try {
			int cnt = this.postMessage(message);

			jmsTemplate.execute(keepAliveTime.getReceiver(),
					new DirectMsgHandler(message));

			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.putIssue(message);

		} catch (Exception e) {
			// TODO runtime exception 처리 필요
			logger.error("error is {}", e);
			throw new RuntimeException(e);

		}

	}
	
	private long getMillisExpiryTime(int sec) {
		return sec * 1000;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#sendUserMessage(kr.co.adflow
	 * .push.domain.ktp.request.UserMessage)
	 */
	public void sendUserMessage(UserMessage userMessage) {

		try {

			Message message = new Message();
			message.setType(CMD_USER_MESSAGE);
			message.setServiceID(DIG_USER_MESSAGE_ACTION_NAME);
			message.setStatus(Message.STATUS_WAIT_FOR_SENDING);

			message.setQos(userMessage.getQos());
			message.setExpiry(this.getMillisExpiryTime(userMessage.getExpiry()));
			message.setAck(true);
			message.setSender(userMessage.getSender());
			message.setReceiver(userMessage.getReceiver());
			message.setContentType(userMessage.getContentType());
			message.setContent(userMessage.getContent());

			int cnt = this.postMessage(message);

			jmsTemplate.execute(message.getReceiver(), new DirectMsgHandler(
					message));

			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.putIssue(message);
		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#validUserID(kr.co.adflow
	 * .push.domain.ktp.request.UserID)
	 */
	public boolean validUserID(UserID userID) {

		boolean result = false;

		try {
			result = tokenDao.validateByUserID(userID.getUserID());
		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);
		}

		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.ktp.service.PlatformService#validUFMI(kr.co.adflow.
	 * push.domain.ktp.request.Ufmi)
	 */
	public boolean validUFMI(Ufmi ufmi) {

		boolean result = false;

		try {
			result = tokenDao.validateByUfmi(ufmi.getUfmi());
		} catch (Exception e) {
			logger.error("error is {}", e);
			throw new RuntimeException(e);
		}

		return result;

	}

}
