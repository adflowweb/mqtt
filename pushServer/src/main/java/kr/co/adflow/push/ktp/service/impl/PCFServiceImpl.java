package kr.co.adflow.push.ktp.service.impl;

import java.io.IOException;
import java.util.Properties;

import kr.co.adflow.push.domain.ktp.Status;
import kr.co.adflow.push.domain.ktp.Subscribe;
import kr.co.adflow.push.handler.AbstractMessageHandler;
import kr.co.adflow.push.ktp.service.PCFService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class PCFServiceImpl implements PCFService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PCFServiceImpl.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(AbstractMessageHandler.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Subscribe[] get(String token) throws Exception {
		logger.debug("get시작(token=" + token + ")");

		Subscribe[] subsList = null;
		try {

			// MQEnvironment.userID = "adflow";
			MQEnvironment.userID = prop.getProperty("mq.pcf.id");
			// MQEnvironment.password = "!ADFlow@";
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			String pcfHost = prop.getProperty("mq.pcf.host");
			int pcfPort = Integer.parseInt(prop.getProperty("mq.pcf.port"));
			String pcfChannel = prop.getProperty("mq.pcf.channel");

			// PCFMessageAgent agent = new PCFMessageAgent("adflow.net", 1414,
			// "ADFlowAdminPCF");
			PCFMessageAgent agent = new PCFMessageAgent(pcfHost, pcfPort,
					pcfChannel);
			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_SUBSCRIPTION);
			request.addParameter(MQConstants.MQCACF_SUB_NAME, token + ":*");

			PCFMessage[] responses = agent.send(request);

			// System.out.println("responses.length ::" + responses.length);
			subsList = new Subscribe[responses.length];
			String topic = "";

			for (int i = 0; i < responses.length; i++) {
				topic = responses[i].getParameterValue(
						MQConstants.MQCA_TOPIC_STRING).toString();

				// topic = topic.substring(token.length() + 1);

				System.out.println("topic :: " + topic);
				subsList[i] = new Subscribe();
				subsList[i].setTopic(topic);
			}
		} catch (PCFException pcfe) {
			if (pcfe.getMessage().indexOf("2428") > 0) {
				System.err
						.println("해당 토큰관련 subscriptions 가 없습니다. -errorcode:2428");

			} else {
				System.err.println("PCF error: " + pcfe);
			}

		} catch (MQException mqe) {
			System.err.println(mqe);
		} catch (IOException ioe) {
			System.err.println(ioe);
		}

		// logger.debug("get종료(Subscribe result=" + subsList + ")");
		return subsList;
	}

	@Override
	public Status getStatus(String token) throws Exception {
		logger.debug("get시작(token=" + token + ")");

		Status status = new Status();
		try {

			// MQEnvironment.userID = "adflow";
			MQEnvironment.userID = prop.getProperty("mq.pcf.id");
			// MQEnvironment.password = "!ADFlow@";
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			String pcfHost = prop.getProperty("mq.pcf.host");
			int pcfPort = Integer.parseInt(prop.getProperty("mq.pcf.port"));
			String pcfChannel = prop.getProperty("mq.pcf.channel");

			// PCFMessageAgent agent = new PCFMessageAgent("adflow.net", 1414,
			// "ADFlowAdminPCF");
			PCFMessageAgent agent = new PCFMessageAgent(pcfHost, pcfPort,
					pcfChannel);
			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
			request.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
			request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE,
					MQConstants.MQCHT_MQTT);
			request.addParameter(MQConstants.MQCACH_CLIENT_ID, token);

			PCFMessage[] responses = agent.send(request);

			int chStatus = ((Integer) (responses[0]
					.getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS)))
					.intValue();

			if (chStatus == 3) {
				status.setStatus("MQTT Connected");
			} else {
				status.setStatus("MQTT Disconnected");

			}

			// String[] chStatusText = {"", "MQCHS_BINDING", "MQCHS_STARTING",
			// "MQCHS_RUNNING",
			// "MQCHS_STOPPING", "MQCHS_RETRYING", "MQCHS_STOPPED",
			// "MQCHS_REQUESTING", "MQCHS_PAUSED",
			// "", "", "", "", "MQCHS_INITIALIZING"};
			// status = chStatusText[chStatus];

		} catch (PCFException pcfe) {
			if (pcfe.getMessage().indexOf("3065") > 0) {
				logger.debug("해당 토큰관련 클라이언트가 Pending 메시지가 없을 경우 채널상태는 없음. -errorcode:3065");
				status.setStatus("MQTT Disconnected");
			} else {
				logger.debug("PCF error: " + pcfe);
				status.setStatus(pcfe.toString());
			}
		} catch (MQException mqe) {
			System.err.println(mqe);
		} catch (IOException ioe) {
			System.err.println(ioe);
		}

		// logger.debug("get종료(Subscribe result=" + subsList + ")");
		return status;
	}

}
