package kr.co.adflow.pms.core.executor;

import java.util.Date;

import kr.co.adflow.pms.core.service.MessageSendService;
import kr.co.adflow.pms.svc.service.PushMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSendExecutor {
	
	private static final int DELAY_TIME = 10000;
	
	private static final int SEND_LIMIT = 100;
	
	private static final String SERVER_ID = "S01";
	
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendExecutor.class);
	
	@Autowired
	private MessageSendService messageSendService;
	
	@Scheduled(fixedDelay=DELAY_TIME)
	public void sendMessageArray() {
		logger.info("sendMessageArray execute time is {}",new Date());
		
		
		messageSendService.sendMessageArray(SERVER_ID,SEND_LIMIT);
		
		
	}

}
