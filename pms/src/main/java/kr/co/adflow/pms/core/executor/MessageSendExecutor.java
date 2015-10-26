/*
 * 
 */
package kr.co.adflow.pms.core.executor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.service.MessageExcutorService;

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
	private MessageExcutorService messageSendService;

	// /** The table mgt mapper. */
	// @Autowired
	// private TableMgtMapper tableMgtMapper;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	// private String hostname;
	// private String serverId;

	// @Scheduled(cron = "#{pms['executor.message.cron']}")
	/**
	 * Send message array.
	 */
	public void sendMessageArray() {
		// logger.info("sendMessageArray execute time is {}", new Date());

		// int executorSendLimit = 1000;
		// if(this.serverId == null){
		// this.serverId = pmsConfig.EXECUTOR_SERVER_ID1;
		// if (this.serverId == null) {
		// if(this.hostname == null) {
		// this.hostname = readHostname();
		// }
		// this.serverId = this.hostname + "01";
		// }else {
		// executorSendLimit = pmsConfig.EXECUTOR_SEND_LIMIT;
		// }
		//
		// }
		// messageSendService.sendMessageArray(this.serverId,
		// executorSendLimit);
		// logger.debug("***********************start");
		// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID1,
		// pmsConfig.EXECUTOR_SEND_LIMIT);
		// logger.debug("***********************end");

	}

	// @Scheduled(cron=PmsConfig.EXECUTOR_RESERVATION_CRON)
	/**
	 * Send reservation message array.
	 */
	public void sendReservationMessageArray() {

		// logger.debug("***********************start");
		// messageSendService.sendReservationMessageArray(
		// pmsConfig.EXECUTOR_SERVER_ID1, pmsConfig.EXECUTOR_SEND_LIMIT);
		// logger.debug("***********************end");

	}

	// /**
	// * Send message array.
	// */
	// public void sendMessageArray2() {
	// //logger.info("sendMessageArray execute time is {}", new Date());
	//
	// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID2,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send reservation message array.
	// */
	// public void sendReservationMessageArray2() {
	// //logger.info("sendReservationMessageArray execute time is {}",
	// // new Date());
	//
	// messageSendService.sendReservationMessageArray(
	// pmsConfig.EXECUTOR_SERVER_ID2, pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send message array.
	// */
	// public void sendMessageArray3() {
	// //logger.info("sendMessageArray execute time is {}", new Date());
	//
	// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID3,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send reservation message array.
	// */
	// public void sendReservationMessageArray3() {
	// //logger.info("sendReservationMessageArray execute time is {}",
	// // new Date());
	//
	// messageSendService.sendReservationMessageArray(
	// pmsConfig.EXECUTOR_SERVER_ID3, pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send message array.
	// */
	// public void sendMessageArray4() {
	// //logger.info("sendMessageArray execute time is {}", new Date());
	//
	// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID4,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send reservation message array.
	// */
	// public void sendReservationMessageArray4() {
	// //logger.info("sendReservationMessageArray execute time is {}",
	// // new Date());
	//
	// messageSendService.sendReservationMessageArray(
	// pmsConfig.EXECUTOR_SERVER_ID4, pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send message array.
	// */
	// public void sendMessageArray5() {
	// //logger.info("sendMessageArray execute time is {}", new Date());
	//
	// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID5,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send reservation message array.
	// */
	// public void sendReservationMessageArray5() {
	// //logger.info("sendReservationMessageArray execute time is {}",
	// // new Date());
	//
	// messageSendService.sendReservationMessageArray(
	// pmsConfig.EXECUTOR_SERVER_ID5, pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// public void sendMessageArray6() {
	// //logger.info("sendMessageArray execute time is {}", new Date());
	//
	// messageSendService.sendMessageArray(pmsConfig.EXECUTOR_SERVER_ID6,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	// /**
	// * Send reservation message array.
	// */
	// public void sendReservationMessageArray6() {
	// //logger.info("sendReservationMessageArray execute time is {}",
	// // new Date());
	//
	// messageSendService.sendReservationMessageArray(
	// pmsConfig.EXECUTOR_SERVER_ID6, pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }
	//
	//
	// // @Scheduled(cron=PmsConfig.EXECUTOR_CALLBACK_CRON)
	// /**
	// * Send callback.
	// */
	// public void sendCallback() {
	// //logger.info("sendCallback execute time is {}", new Date());
	//
	// messageSendService.sendCallback(pmsConfig.EXECUTOR_SERVER_ID1,
	// pmsConfig.EXECUTOR_SEND_LIMIT);
	//
	// }

	// @Scheduled(cron="0 0 12 * * *")
	// @Scheduled(cron=PmsConfig.EXECUTOR_CREATE_TABLE_CRON)
	/**
	 * Creates the table.
	 */
	// public void createTable() {
	//
	// String name = DateUtil.getYYYYMM(1);
	//
	// // logger.info("createTable1");
	//
	// try {
	// tableMgtMapper.selectMessage(name);
	// logger.info("selectMessage");
	// } catch (Exception e) {
	// tableMgtMapper.createMessage(name);
	// logger.info("createMessage");
	// }
	// logger.info("createTable2");
	// try {
	// tableMgtMapper.selectContent(name);
	// logger.info("selectContent");
	// } catch (Exception e) {
	// tableMgtMapper.createContent(name);
	// logger.info("createContent");
	// }
	// logger.info("createTable3");
	// try {
	// tableMgtMapper.selectAck(name);
	// logger.info("selectAck");
	// } catch (Exception e) {
	// tableMgtMapper.createAck(name);
	// logger.info("createAck");
	// }
	//
	// logger.info("createTable4");
	// try {
	// tableMgtMapper.selectGroupMessage(name);
	// logger.info("selectGroupMessage");
	// } catch (Exception e) {
	// tableMgtMapper.createGroupMessage(name);
	// logger.info("createGroupMessage");
	// }
	//
	// }

	public String readHostname() {
		String lineStr = "";
		String hName = "";
		Process process;
		try {
			process = Runtime.getRuntime().exec("hostname");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
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
