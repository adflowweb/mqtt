package kr.co.adflow.push.ktp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import scala.annotation.meta.getter;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.ktp.controller.PlatformController;
import kr.co.adflow.push.ktp.handler.DigInfoUpdateHandler;
import kr.co.adflow.push.ktp.handler.DirectMsgHandler;
import kr.co.adflow.push.ktp.handler.FwInfoUpgradeHandler;
import kr.co.adflow.push.ktp.handler.KeepAliveTimeUpdateHandler;
import kr.co.adflow.push.ktp.handler.PreCheckHandler;
import kr.co.adflow.push.ktp.service.PlatformService;

@Service
public class PlatformServiceImpl implements PlatformService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(PlatformServiceImpl.class);

	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void sendPrecheck(String topicName) {
		
		logger.info("유저=" + topicName);
		System.out.println("topic=" + topicName);
		
		jmsTemplate.execute(topicName, new PreCheckHandler());

	}
	
	public void modifyFwInfo(FwInfo fwInfo) {
		
		logger.info("유저=" + fwInfo.getReceiver());
		System.out.println("topic=" + fwInfo.getReceiver());
		
		Message msg = new Message();
		
		msg.setReceiver(fwInfo.getReceiver());
		msg.setContent(fwInfo.getContent());
		
		jmsTemplate.execute(fwInfo.getReceiver(), new FwInfoUpgradeHandler(msg));

	}
	
	public void modifyDigInfo(DigInfo digInfo) {
		
		logger.info("유저=" + digInfo.getReceiver());
		System.out.println("topic=" + digInfo.getReceiver());
		
		Message msg = new Message();
		
		msg.setReceiver(digInfo.getReceiver());
		msg.setContent(digInfo.getContent());
		
		jmsTemplate.execute(digInfo.getReceiver(), new DigInfoUpdateHandler(msg));

	}
	
	public void sendMessage(Message message) {
		
		logger.info("유저=" + message.getReceiver());
		System.out.println("topic=" + message.getReceiver());
		
		jmsTemplate.execute(message.getReceiver(), new DirectMsgHandler(message));

	}
	
	public void modifyKeepAliveTime(KeepAliveTime keepAliveTime) {
		
		logger.info("유저=" + keepAliveTime.getReceiver());
		System.out.println("topic=" + keepAliveTime.getReceiver());
		
		Message msg = new Message();
		
		msg.setSender(keepAliveTime.getSender());
		msg.setReceiver(keepAliveTime.getReceiver());
		msg.setContent(keepAliveTime.getContent());
		
		jmsTemplate.execute(keepAliveTime.getReceiver(), new KeepAliveTimeUpdateHandler(msg));
		
	}
	
	
	
	
	

}
