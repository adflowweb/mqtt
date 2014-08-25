package kr.co.adflow.push.ktp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.ktp.Subscribe;
import kr.co.adflow.push.handler.AbstractMessageHandler;
import kr.co.adflow.push.ktp.service.SubscribeService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.pcf.CMQCFC;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class SubscribeServiceImpl implements SubscribeService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SubscribeServiceImpl.class);
	
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
			
//			MQEnvironment.userID = "adflow";
			MQEnvironment.userID = prop.getProperty("mq.pcf.id");
//			MQEnvironment.password = "!ADFlow@";
			MQEnvironment.password = prop.getProperty("mq.pcf.password");
			
			String pcfHost = prop.getProperty("mq.pcf.host");
			int pcfPort = Integer.parseInt(prop.getProperty("mq.pcf.port"));
			String pcfChannel = prop.getProperty("mq.pcf.channel");
			
//			PCFMessageAgent agent = new PCFMessageAgent("adflow.net", 1414, "ADFlowAdminPCF");
			PCFMessageAgent agent = new PCFMessageAgent(pcfHost, pcfPort, pcfChannel);
			PCFMessage request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_SUBSCRIPTION);
			request.addParameter(CMQCFC.MQCACF_SUB_NAME, token+":*");

			PCFMessage[] responses = agent.send(request);

			System.out.println("responses.length ::" + responses.length);
			subsList = new Subscribe[responses.length];
			String topic = "";
			
			for (int i = 0; i < responses.length; i++) {
				topic = responses[i].getParameterValue(CMQCFC.MQCACF_SUB_NAME).toString();

				topic = topic.substring(token.length() + 1);
				
				System.out.println("topic :: " + topic);
				subsList[i] = new Subscribe();
				subsList[i].setTopic(topic);
			}
		} catch (PCFException pcfe) {
			System.err.println("PCF error: " + pcfe);
		} catch (MQException mqe) {
			System.err.println(mqe);
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		
		
		
		
//		logger.debug("get종료(Subscribe result=" + subsList + ")");
		return subsList;
	}

}
