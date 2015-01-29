package kr.co.adflow.pms.core.executor;

import java.util.Date;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.service.MessageSendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSendExecutor {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendExecutor.class);

	@Autowired
	private MessageSendService messageSendService;

	@Scheduled(fixedDelay = PmsConfig.EXECUTOR_DELAY_TIME)
	public void sendMessageArray() {
		logger.info("sendMessageArray execute time is {}", new Date());

		messageSendService.sendMessageArray(PmsConfig.EXECUTOR_SERVER_ID,
				PmsConfig.EXECUTOR_SEND_LIMIT);

	}

}
