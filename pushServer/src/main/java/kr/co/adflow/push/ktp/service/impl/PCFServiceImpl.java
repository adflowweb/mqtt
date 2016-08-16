/*
 * 
 */
package kr.co.adflow.push.ktp.service.impl;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.resource.spi.ConnectionManager;

import kr.co.adflow.push.domain.ktp.Status;
import kr.co.adflow.push.handler.AbstractMessageHandler;
import kr.co.adflow.push.ktp.service.PCFService;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

// TODO: Auto-generated Javadoc
/**
 * The Class PCFServiceImpl.
 * 
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Service
public class PCFServiceImpl implements PCFService {

	private JedisPool jedisPool = null;

	@PostConstruct
	public void initRedisPool() {
		jedisPool = new JedisPool(new JedisPoolConfig(), prop.getProperty("jedis.hosts"));
		System.out.println("=== JdeisPool Load OK ===");

	}

	@PreDestroy
	public void releaseRedisPool() {
		jedisPool.destroy();
		System.out.println("=== JdeisPool Release OK ===");
	}

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PCFServiceImpl.class);

	/** The Constant CONFIG_PROPERTIES. */
	private static final String CONFIG_PROPERTIES = "/config.properties";

	/** The prop. */
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(AbstractMessageHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.ktp.service.PCFService#get(java.lang.String)
	 */
	@Override
	public String[] get(String token) throws Exception {
		logger.debug("== get시작(token={})", token);

		String[] subsList = null;

		Jedis jedis = null;

		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 테스트 코드 추후삭제
		jedis = null;
		/////////////////
		if (jedis != null) {
			Boolean jedisOK = false;

			try {
				jedisOK = jedis.exists(token);

				if (jedisOK) {

					String jedisData = jedis.get(token);
					logger.debug("Token:" + token + ", list:" + jedisData);
					Object obj = JSONValue.parse(jedisData);
					JSONArray array = (JSONArray) obj;

					// subList = array.toArray;
					subsList = new String[array.size()];

					for (int i = 0; i < array.size(); i++) {
						subsList[i] = (String) array.get(i);
					}

				} else {
					subsList = callPCF(token);

					JSONArray jsonList = new JSONArray();

					for (int i = 0; i < subsList.length; i++) {
						jsonList.add(subsList[i]);
					}

					jedis.set(token, JSONValue.toJSONString(jsonList));
					jedis.expire(token, Integer.parseInt(prop.getProperty("jedis.expire")));
				}

			} catch (JedisConnectionException e) {
				if (null != jedis) {
					jedisPool.returnBrokenResource(jedis);
					jedis = null;
				}

				subsList = callPCF(token);

			} finally {
				if (null != jedis) {
					jedisPool.returnResource(jedis);
				}
			}

		} else {

			subsList = callPCF(token);

		}

		// logger.debug("get종료(Subscribe result=" + subsList + ")");
		return subsList;
	}

	private String[] callPCF(String token) throws MQException, IOException {
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		String[] subsList = null;

		try {

			qmgr = new MQQueueManager("*", connMan);

			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_SUBSCRIPTION);
			request.addParameter(MQConstants.MQCACF_SUB_NAME, token + ":*");

			PCFMessage[] responses = agent.send(request);

			// System.out.println("responses.length ::" + responses.length);
			subsList = new String[responses.length];
			// String topic = "";

			for (int i = 0; i < responses.length; i++) {
				subsList[i] = responses[i].getParameterValue(MQConstants.MQCA_TOPIC_STRING).toString();
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
		return subsList;
	}

	// /* (non-Javadoc)
	// * @see kr.co.adflow.push.ktp.service.PCFService#get(java.lang.String)
	// */
	// @Override
	// public String[] get(String token) throws Exception {
	// logger.debug("get시작(token={})",token);
	//
	// String[] subsList = null;
	// PCFMessageAgent agent = null;
	// try {
	//
	// // MQEnvironment.userID = "adflow";
	// MQEnvironment.userID = prop.getProperty("mq.pcf.id");
	// // MQEnvironment.password = "!ADFlow@";
	// MQEnvironment.password = prop.getProperty("mq.pcf.password");
	//
	// String pcfHost = prop.getProperty("mq.pcf.host");
	// int pcfPort = Integer.parseInt(prop.getProperty("mq.pcf.port"));
	// String pcfChannel = prop.getProperty("mq.pcf.channel");
	//
	// // PCFMessageAgent agent = new PCFMessageAgent("adflow.net", 1414,
	// // "ADFlowAdminPCF");
	// agent = new PCFMessageAgent(pcfHost, pcfPort,
	// pcfChannel);
	// PCFMessage request = new PCFMessage(
	// MQConstants.MQCMD_INQUIRE_SUBSCRIPTION);
	// request.addParameter(MQConstants.MQCACF_SUB_NAME, token + ":*");
	//
	// PCFMessage[] responses = agent.send(request);
	//
	// // System.out.println("responses.length ::" + responses.length);
	// subsList = new String[responses.length];
	// //String topic = "";
	//
	// for (int i = 0; i < responses.length; i++) {
	// subsList[i] = responses[i].getParameterValue(
	// MQConstants.MQCA_TOPIC_STRING).toString();
	//
	// // topic = topic.substring(token.length() + 1);
	//
	// // System.out.println("topic :: " + topic);
	// // subsList[i] = new Subscribe();
	// // subsList[i].setTopic(topic);
	// }
	//
	// } catch (PCFException pcfe) {
	// if (pcfe.getMessage().indexOf("2428") > 0) {
	// logger.error("해당 토큰관련 subscriptions 가 없습니다. -errorcode:2428");
	//
	// } else {
	// logger.error("PCF error: " + pcfe);
	// }
	//
	// } catch (MQException mqe) {
	// logger.error("MQException is ",mqe);
	// throw mqe;
	// } catch (IOException ioe) {
	// logger.error("IOException is ",ioe);
	// throw ioe;
	// } finally {
	//
	// if (agent != null) {
	// logger.info("agent disconnect" );
	// agent.disconnect();
	// } else {
	// logger.info("agent null" );
	// }
	// }
	//
	// // logger.debug("get종료(Subscribe result=" + subsList + ")");
	// return subsList;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.ktp.service.PCFService#getStatus(java.lang.String)
	 */
	@Override
	public Status getStatus(String token) throws Exception {
		logger.debug("get시작(token=" + token + ")");

		Status status = new Status();
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {

			qmgr = new MQQueueManager("*", connMan);

			agent = new PCFMessageAgent(qmgr);

			PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
			request.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
			request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_MQTT);
			request.addParameter(MQConstants.MQCACH_CLIENT_ID, token);

			PCFMessage[] responses = agent.send(request);

			int chStatus = ((Integer) (responses[0].getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS))).intValue();

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

		// logger.debug("get종료(Subscribe result=" + subsList + ")");
		return status;
	}

	// /* (non-Javadoc)
	// * @see
	// kr.co.adflow.push.ktp.service.PCFService#getStatus(java.lang.String)
	// */
	// @Override
	// public Status getStatus(String token) throws Exception {
	// logger.debug("get시작(token=" + token + ")");
	//
	// Status status = new Status();
	// PCFMessageAgent agent = null;
	// try {
	//
	// // MQEnvironment.userID = "adflow";
	// MQEnvironment.userID = prop.getProperty("mq.pcf.id");
	// // MQEnvironment.password = "!ADFlow@";
	// MQEnvironment.password = prop.getProperty("mq.pcf.password");
	//
	// String pcfHost = prop.getProperty("mq.pcf.host");
	// int pcfPort = Integer.parseInt(prop.getProperty("mq.pcf.port"));
	// String pcfChannel = prop.getProperty("mq.pcf.channel");
	//
	// // PCFMessageAgent agent = new PCFMessageAgent("adflow.net", 1414,
	// // "ADFlowAdminPCF");
	// agent = new PCFMessageAgent(pcfHost, pcfPort,
	// pcfChannel);
	// PCFMessage request = new PCFMessage(
	// MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
	// request.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
	// request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE,
	// MQConstants.MQCHT_MQTT);
	// request.addParameter(MQConstants.MQCACH_CLIENT_ID, token);
	//
	// PCFMessage[] responses = agent.send(request);
	//
	// int chStatus = ((Integer) (responses[0]
	// .getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS)))
	// .intValue();
	//
	// if (chStatus == 3) {
	// status.setStatus("MQTT Connected");
	// } else {
	// status.setStatus("MQTT Disconnected");
	//
	// }
	//
	// // String[] chStatusText = {"", "MQCHS_BINDING", "MQCHS_STARTING",
	// // "MQCHS_RUNNING",
	// // "MQCHS_STOPPING", "MQCHS_RETRYING", "MQCHS_STOPPED",
	// // "MQCHS_REQUESTING", "MQCHS_PAUSED",
	// // "", "", "", "", "MQCHS_INITIALIZING"};
	// // status = chStatusText[chStatus];
	//
	// } catch (PCFException pcfe) {
	// if (pcfe.getMessage().indexOf("3065") > 0) {
	// logger.debug("해당 토큰관련 클라이언트가 Pending 메시지가 없을 경우 채널상태는 없음.
	// -errorcode:3065");
	// status.setStatus("MQTT Disconnected");
	// } else {
	// logger.debug("PCF error: " + pcfe);
	// status.setStatus(pcfe.toString());
	// }
	// } catch (MQException mqe) {
	// logger.error("MQException is",mqe);
	// throw mqe;
	// } catch (IOException ioe) {
	// logger.error("IOException is",ioe);
	// throw ioe;
	// } finally {
	// if (agent != null) {
	// logger.info("agent disconnect" );
	// agent.disconnect();
	// } else {
	// logger.info("agent null" );
	// }
	// }
	//
	// // logger.debug("get종료(Subscribe result=" + subsList + ")");
	// return status;
	// }

}