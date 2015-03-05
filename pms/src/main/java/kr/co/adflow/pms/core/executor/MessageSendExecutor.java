/*
 * 
 */
package kr.co.adflow.pms.core.executor;

import java.util.Date;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.service.MessageSendService;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.mapper.TableMgtMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageSendExecutor.
 */
@Component("messageSendExecutor")
public class MessageSendExecutor {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendExecutor.class);

	/** The message send service. */
	@Autowired
	private MessageSendService messageSendService;

	/** The table mgt mapper. */
	@Autowired
	private TableMgtMapper tableMgtMapper;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	// @Scheduled(cron = "#{pms['executor.message.cron']}")
	/**
	 * Send message array.
	 */
	public void sendMessageArray() {
		logger.info("sendMessageArray execute time is {}", new Date());

		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID,
				pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_RESERVATION_CRON)
	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray() {
		logger.info("sendReservationMessageArray execute time is {}",
				new Date());

		messageSendService.sendReservationMessageArray(
				pmsConfig.EXECUTOR_SERVER_ID, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_CALLBACK_CRON)
	/**
	 * Send callback.
	 */
	public void sendCallback() {
		logger.info("sendCallback execute time is {}", new Date());

		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID,
				pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron="0 0 12 * * *")
	// @Scheduled(cron=PmsConfig.EXECUTOR_CREATE_TABLE_CRON)
	/**
	 * Creates the table.
	 */
	public void createTable() {

		String name = DateUtil.getYYYYMM(1);

		logger.info("createTable1");

		try {
			tableMgtMapper.selectMessage(name);
			logger.info("selectMessage");
		} catch (Exception e) {
			tableMgtMapper.createMessage(name);
			logger.info("createMessage");
		}
		logger.info("createTable2");
		try {
			tableMgtMapper.selectContent(name);
			logger.info("selectContent");
		} catch (Exception e) {
			tableMgtMapper.createContent(name);
			logger.info("createContent");
		}
		logger.info("createTable3");
		try {
			tableMgtMapper.selectAck(name);
			logger.info("selectAck");
		} catch (Exception e) {
			tableMgtMapper.createAck(name);
			logger.info("createAck");
		}

	}

}
