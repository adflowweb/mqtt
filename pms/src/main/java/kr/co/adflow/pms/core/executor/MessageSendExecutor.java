/*
 * 
 */
package kr.co.adflow.pms.core.executor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.handler.ZookeeperHandler;
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
	private static final Logger logger = LoggerFactory.getLogger(MessageSendExecutor.class);

	/** The message send service. */
	@Autowired
	private MessageSendService messageSendService;

	/** The table mgt mapper. */
	@Autowired
	private TableMgtMapper tableMgtMapper;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	@Autowired
	ZookeeperHandler zookeeperHandler;

	// @Scheduled(cron = "#{pms['executor.message.cron']}")
	/**
	 * Send message array.
	 */
	public void sendMessageArray1() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray1()  나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray1() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_RESERVATION_CRON)
	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray1() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray1() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray1() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send message array.
	 */
	public void sendMessageArray2() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray2() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray2() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray2() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray2() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray2() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send message array.
	 */
	public void sendMessageArray3() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray3() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + " sendMessageArray3() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray3() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray3() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendReservationMessageArray3() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID,
				pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_CALLBACK_CRON)
	/**
	 * Send callback.
	 */
	public void sendCallback1() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback() 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send callback.
	 */
	public void sendCallback2() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback()2 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback()2 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send callback.
	 */
	public void sendCallback3() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback()3 나는 리더가 아닙니다");
			return;
		}
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "sendCallback()3 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron="0 0 12 * * *")
	// @Scheduled(cron=PmsConfig.EXECUTOR_CREATE_TABLE_CRON)
	/**
	 * Creates the table.
	 */
	public void createTable() {

		String name = DateUtil.getYYYYMM(1);

		// logger.info("createTable1");

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

		logger.info("createTable4");
		try {
			tableMgtMapper.selectGroupMessage(name);
			logger.info("selectGroupMessage");
		} catch (Exception e) {
			tableMgtMapper.createGroupMessage(name);
			logger.info("createGroupMessage");
		}

	}

	public String readHostname() {
		String lineStr = "";
		String hName = "";
		Process process;
		try {
			process = Runtime.getRuntime().exec("hostname");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((lineStr = br.readLine()) != null) {
				hName = lineStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			hName = "none";
		}
		return hName;
	}

}
