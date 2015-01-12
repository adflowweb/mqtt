package kr.co.adflow.push.ktp.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.jms.DeliveryMode;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.ktp.handler.DirectMsgHandler;
import kr.co.adflow.push.ktp.handler.PreCheckHandler;
import kr.co.adflow.push.ktp.service.PlatformService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlatformServiceImpl implements PlatformService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(PlatformServiceImpl.class);
	
	private static final int CMD_KEEP_ALIVE_TIME = 102;
	private static final int CMD_PRE_CHECK = 103;
	private static final int CMD_FW_UPGRADE = 104;
	private static final int CMD_PTT_UPDATE = 105;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource
	MessageDao messageDao;
	
	private final static int TIME_TO_LIVE = 3000;
	
	//private @Value("#{code['precheck.time.to.live']}") int TIME_TO_LIVE;
	
	public void sendPrecheck(String topicName) {
		
		jmsTemplate.execute(topicName, new PreCheckHandler(TIME_TO_LIVE));

	}
	
	public void modifyFwInfo(FwInfo fwInfo) {
		
		Message msg = new Message();
		
		msg.setType(CMD_FW_UPGRADE);
		msg.setStatus(Message.STATUS_PUSH_SENT);
		msg.setQos(DeliveryMode.PERSISTENT);
		msg.setIssue(new Date());
		
		msg.setSender(fwInfo.getSender());
		msg.setReceiver(fwInfo.getReceiver());
		msg.setContent(fwInfo.getContent());
		
		jmsTemplate.execute(fwInfo.getReceiver(), new DirectMsgHandler(msg));
		
		try {
			int cnt = messageDao.post(msg);
		} catch (Exception e) {
			logger.error("error is {}", e);
			
		}

	}
	
	public void modifyDigInfo(DigInfo digInfo) {

		Message msg = new Message();
		
		msg.setType(CMD_PTT_UPDATE);
		msg.setStatus(Message.STATUS_PUSH_SENT);
		msg.setQos(DeliveryMode.PERSISTENT);
		msg.setIssue(new Date());
		
		msg.setSender(digInfo.getSender());
		msg.setReceiver(digInfo.getReceiver());
		msg.setContent(digInfo.getContent());
		
		jmsTemplate.execute(digInfo.getReceiver(), new DirectMsgHandler(msg));
		
		try {
			System.out.println("msg="+msg.getIssue());
			int cnt = messageDao.post(msg);
			System.out.println("cnt="+cnt);
		} catch (Exception e) {
			logger.error("error is {}", e);
			
		}

	}
	
	public void sendMessage(Message message) {

		message.setStatus(Message.STATUS_PUSH_SENT);
		message.setIssue(new Date());

		jmsTemplate.execute(message.getReceiver(), new DirectMsgHandler(message));
		
		try {
			System.out.println("msg="+message.getIssue());
			int cnt = messageDao.post(message);
			System.out.println("cnt="+cnt);
		} catch (Exception e) {
			logger.error("error is {}", e);
			
		}

	}
	
	public void modifyKeepAliveTime(KeepAliveTime keepAliveTime) {
		
		Message msg = new Message();
		
		msg.setType(CMD_KEEP_ALIVE_TIME);
		msg.setStatus(Message.STATUS_PUSH_SENT);
		msg.setQos(DeliveryMode.PERSISTENT);
		msg.setIssue(new Date());
		
		msg.setSender(keepAliveTime.getSender());
		msg.setReceiver(keepAliveTime.getReceiver());
		msg.setContent(keepAliveTime.getContent());
		
		jmsTemplate.execute(keepAliveTime.getReceiver(), new DirectMsgHandler(msg));
		
		try {
			System.out.println("msg="+msg.getIssue());
			int cnt = messageDao.post(msg);
			System.out.println("cnt="+cnt);
		} catch (Exception e) {
			//TODO runtime exception 처리 필요
			logger.error("error is {}", e);
			
		}
		
	}
	
	public void sendUserMessage(Message message) {
		
		try {
			message.setStatus(Message.STATUS_WAIT_FOR_SENDING);
			int cnt = messageDao.post(message);
			
			jmsTemplate.execute(message.getReceiver(), new DirectMsgHandler(message));
			
			message.setStatus(Message.STATUS_PUSH_SENT);
			message.setIssue(new Date());
			messageDao.post(message);
			System.out.println("cnt="+cnt);
		} catch (Exception e) {
			logger.error("error is {}", e);
			
		}

	}
	
	
	
	

}
