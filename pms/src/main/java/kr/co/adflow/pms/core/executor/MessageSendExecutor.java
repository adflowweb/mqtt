package kr.co.adflow.pms.core.executor;

import java.util.Date;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.service.MessageSendService;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.mapper.TableMgtMapper;

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
	
	@Autowired
	private TableMgtMapper tableMgtMapper;

	@Scheduled(fixedDelay = PmsConfig.EXECUTOR_DELAY_TIME)
	public void sendMessageArray() {
		logger.info("sendMessageArray execute time is {}", new Date());

		messageSendService.sendMessageArray(PmsConfig.EXECUTOR_SERVER_ID,
				PmsConfig.EXECUTOR_SEND_LIMIT);

	}
	
	@Scheduled(fixedDelay = PmsConfig.EXECUTOR_DELAY_TIME)
	public void sendReservationMessageArray() {
		logger.info("sendReservationMessageArray execute time is {}", new Date());

		messageSendService.sendReservationMessageArray(PmsConfig.EXECUTOR_SERVER_ID,
				PmsConfig.EXECUTOR_SEND_LIMIT);

	}
	
	@Scheduled(cron="0 * * * * *")
	public void createTable() {
		
		String name = DateUtil.getYYYYMM(0);
		
		logger.info("createTable1");
		
		try {
			tableMgtMapper.selectMessage(name);
			logger.info("selectMessage");
		} catch(Exception e) {
			tableMgtMapper.createMessage(name);
			logger.info("createMessage");
		}
		logger.info("createTable2");
		try {
			tableMgtMapper.selectContent(name);
			logger.info("selectContent");
		} catch(Exception e) {
			tableMgtMapper.createContent(name);
			logger.info("createContent");
		}
		logger.info("createTable3");
		try {
			tableMgtMapper.selectAck(name);
			logger.info("selectAck");
		} catch(Exception e) {
			tableMgtMapper.createAck(name);
			logger.info("createAck");
		}
		
		
	}

}