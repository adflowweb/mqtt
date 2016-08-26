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
	public void sendMessageArrayOld() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(" sendMessageArrayOld() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(" sendMessageArrayOld() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_RESERVATION_CRON)
	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArrayOld() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendReservationMessageArrayOld() 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendReservationMessageArrayOld() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID,
				pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send message array.
	 */
	public void sendMessageArray1() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(" sendMessageArray1() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(" sendMessageArray1() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray1() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendReservationMessageArray1() 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendReservationMessageArray1() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send message array.
	 */
	public void sendMessageArray2() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug(" sendMessageArray2() 나는 리더가 아닙니다");
			return;
		}
		logger.debug(" sendMessageArray2() 현재 리더 입니다!");
		messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray2() {
		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendReservationMessageArray2() 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendReservationMessageArray2() 현재 리더 입니다!");
		messageSendService.sendReservationMessageArray(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_CALLBACK_CRON)
	/**
	 * Send callback.
	 */
	public void sendCallbackOld() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendCallback() 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendCallback() 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send callback.
	 */
	public void sendCallback1() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendCallback()2 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendCallback()2 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);

	}

	/**
	 * Send callback.
	 */
	public void sendCallback2() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug("sendCallback()3 나는 리더가 아닙니다");
			return;
		}
		logger.debug("sendCallback()3 현재 리더 입니다!");
		messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);

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
