package kr.co.adflow.pms.core.service;

import java.io.IOException;

import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

@Service
public class PCFServiceImpl implements PCFService {

	private static final Logger logger = LoggerFactory
			.getLogger(PCFServiceImpl.class);

	@SuppressWarnings("deprecation")
	@Override
	public String getStatus(String token) throws Exception {
		logger.debug("token:" + token);

		String statusResult = null;
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {
			qmgr = new MQQueueManager("MQTT", connMan);
			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
			request.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
			request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE,
					MQConstants.MQCHT_MQTT);
			request.addParameter(MQConstants.MQCACH_CLIENT_ID, token);

			PCFMessage[] response = agent.send(request);

			int chStatus = (int) response[0]
					.getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS);
			if (chStatus == 3) {

				statusResult = "MQTT Connected";
			} else {

				statusResult = "MQTT Disconnected";
			}

		} catch (PCFException pcfe) {
			if (pcfe.getMessage().indexOf("3065") > 0) {
				logger.debug("해당 토큰관련 클라이언트가 Pending 메시지가 없을 경우 채널상태는 없음. -errorcode:3065");
				statusResult = "MQTT Disconnected";
			} else {
				logger.debug("PCF error: " + pcfe);
				statusResult = pcfe.toString();
			}
		} catch (MQException mqe) {
			logger.error("MQException is", mqe);
			throw mqe;
		} catch (IOException ioe) {
			logger.error("IOException is", ioe);
			throw ioe;
		} finally {
			if (agent != null) {
				try {
					agent.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
		}

		return statusResult;
	}

	@Override
	public String[] getTopics(String token) throws Exception {
		logger.debug("token:" + token);
		String[] topicArr = null;
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {

			qmgr = new MQQueueManager("MQTT", connMan);
			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_SUBSCRIPTION);
			request.addParameter(MQConstants.MQCACF_SUB_NAME, token + ":*");
			PCFMessage[] response = agent.send(request);
			topicArr = new String[response.length];
			for (int i = 0; i < response.length; i++) {
				topicArr[i] = response[i].getParameterValue(
						MQConstants.MQCA_TOPIC_STRING).toString();
			}

		} catch (PCFException pcfe) {

			if (pcfe.getMessage().indexOf("2428") > 0) {
				logger.error("해당 토큰관련 subscriptions 가 없습니다. -errorcode:2428");

			} else {
				logger.error("PCF error: " + pcfe);
			}

		} catch (MQException mqe) {
			logger.error("MQException is ", mqe);
			throw mqe;
		} catch (IOException ioe) {
			logger.error("IOException is ", ioe);
			throw ioe;
		} finally {
			if (agent != null) {
				try {
					agent.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}

		}

		return topicArr;
	}
}
