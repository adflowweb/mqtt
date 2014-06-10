package kr.co.adflow.push.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.exception.PushException;
import kr.co.adflow.push.service.MqttService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Service
public class MqttServiceImpl implements Runnable, MqttCallback, MqttService {

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MqttServiceImpl.class);
	// 23 character 로 제한됨
	private static final int CLIENT_ID_LENGTH = 23;
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MqttServiceImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	private static final String[] TOPIC = prop.getProperty("topic").split(",");
	private static final String SERVERURL = prop.getProperty("mq.server.url");
	private static String CLIENTID;// prop.getProperty("clientid");
	static {
		if (prop.getProperty("clientid") == null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(InetAddress.getLocalHost().getHostName().getBytes());
				byte[] mdbytes = md.digest();

				// convert the byte to hex format method 1
				StringBuffer sb = new StringBuffer();
				logger.debug("mdbytes.length=" + mdbytes.length);
				for (int i = 0; i < mdbytes.length; i++) {
					sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
							.substring(1));
				}
				logger.debug("deviceIDHexString=" + sb);
				CLIENTID = sb.toString().substring(0, CLIENT_ID_LENGTH);
				logger.debug("clientID=" + CLIENTID);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			CLIENTID = prop.getProperty("clientid");
		}

	}

	private ScheduledExecutorService scheduledExecutorService;
	private MqttClient mqttClient;
	private int healthCheckInterval = Integer.parseInt(prop
			.getProperty("health.check.interval"));
	private int connectionTimeout = Integer.parseInt(prop
			.getProperty("connection.timeout"));
	private int keepAliveInterval = Integer.parseInt(prop
			.getProperty("keep.alive.interval"));
	private boolean cleanSession = Boolean.parseBoolean(prop
			.getProperty("clean.session"));
	private MqttConnectOptions mOpts;
	private String errorMsg;
	private double reqCnt; // receive message count
	private double tps; // 10초 평균 tps

	private Level logLevel = Level.parse(prop.getProperty("paho.log.level"));

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("initIt시작()");
		setMqttClientLog();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleWithFixedDelay(this, 0,
				healthCheckInterval, TimeUnit.SECONDS);
		logger.info("excutorService가시작되었습니다.");
		mOpts = makeMqttOpts();
		logger.info("initIt종료()");
	}

	/**
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		scheduledExecutorService.shutdown();
		logger.info("excutorService가종료되었습니다.");
		destroy();
		logger.info("cleanUp종료()");
	}

	private void setMqttClientLog() {
		logger.debug("setMqttClientLog시작()");
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger rootLogger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(logLevel);
		rootLogger.setLevel(logLevel);
		rootLogger.addHandler(defaultHandler);
		logManager.addLogger(rootLogger);
		logger.debug("setMqttClientLog종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.debug("run시작()");
		try {
			if (mqttClient == null) {
				logger.debug("mqtt연결을처음시도합니다.");
				connect();
				logger.debug("topic.length=" + TOPIC.length);
				for (int i = 0; i < TOPIC.length; i++) {
					subscribe(TOPIC[i], 2);
				}

			} else if (!mqttClient.isConnected()) {
				logger.debug("서버와연결되어있지않으므로접속을시도합니다.");
				reconnect();
				for (int i = 0; i < TOPIC.length; i++) {
					subscribe(TOPIC[i], 2);
				}
			}
			errorMsg = null;
		} catch (Exception e) {
			errorMsg = e.toString();
			logger.error("에러발생", e);
		}

		// tps 계산
		tps = reqCnt / (double) 10;
		logger.debug("reqCnt=" + reqCnt);
		logger.debug("tps=" + tps);
		reqCnt = 0; // 초기화
		logger.debug("run종료()");
	}

	/**
	 * @throws MqttException
	 */
	private synchronized void reconnect() throws MqttException {
		logger.debug("reconnect시작()");
		logger.debug("serverURL=" + SERVERURL);
		logger.debug("clientID=" + CLIENTID);
		mqttClient.connect(mOpts);
		// 리커넥트시에 초기시도했던 상태값들이 정상적으로 되어있는지
		// 커넥트옵션이나 서브스크라이브가 정확히 되어있는지 상태 체크바람
		logger.debug("세션연결을완료하였습니다.");
		logger.debug("reconnect종료()");
	}

	/**
	 * @return
	 */
	private MqttConnectOptions makeMqttOpts() {
		logger.debug("makeMqttOpts시작()");
		MqttConnectOptions mOpts = new MqttConnectOptions();
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(cleanSession);
		// mOpts.setUserName("mqttPushServer");
		// mOpts.setPassword("password".toCharArray());
		logger.debug("makeMqttOpts종료(mOpts=" + mOpts + ")");
		return mOpts;
	}

	/**
	 * @throws MqttException
	 */
	private synchronized void connect() throws MqttException {
		logger.debug("connect시작()");
		logger.debug("serverURL=" + SERVERURL);
		logger.debug("clientID=" + CLIENTID);
		mqttClient = new MqttClient(SERVERURL, CLIENTID);
		logger.debug("mqttClient인스턴스가생성되었습니다.::" + mqttClient);
		mqttClient.setCallback(this);
		mqttClient.connect(mOpts);
		logger.debug("세션연결을완료하였습니다.");
		logger.debug("connect종료()");
	}

	/**
	 * @param topic
	 * @param qos
	 * @throws MqttException
	 */
	private synchronized void subscribe(String topic, int qos)
			throws MqttException {
		logger.debug("subscribe시작(topic=" + topic + ",qos=" + qos + ")");
		mqttClient.subscribe(topic, qos);
		logger.debug("토픽구독을완료하였습니다");
		logger.debug("subscribe종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	@Override
	public void connectionLost(Throwable throwable) {
		logger.debug("connectionLost시작(throwable=" + throwable + ")");
		errorMsg = throwable.toString();
		logger.error("mqttTCP세션연결이끊겼습니다.", throwable);
		logger.debug("connectionLost종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse
	 * .paho.client.mqttv3.IMqttDeliveryToken)
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.debug("deliveryComplete시작(deliveryComplete=" + token + ")");
		logger.debug("deliveryComplete종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		logger.debug("messageArrived시작(topic=" + topic + ",message=" + message
				+ ",qos=" + message.getQos() + ")");
		reqCnt++;
		logger.debug("messageArrived종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * mqttClient를 종료하는 메소드. mqttClient가 재활용되기때문에 최후에 정리하고 종료되어야합니다.
	 * 
	 * @see kr.co.adflow.push.service.ConnectionService#destroy()
	 */
	public synchronized void destroy() throws Exception {
		logger.debug("destroy시작()");
		logger.debug("mqttClient=" + mqttClient);
		if (mqttClient == null) {
			logger.debug("destroy종료()");
			return;
		}
		logger.debug("mqttClient세션연결상태="
				+ (mqttClient.isConnected() ? "연결됨" : "끊어짐"));
		if (mqttClient.isConnected()) {
			mqttClient.disconnect();
			logger.debug("mqttClient연결을끊었습니다.");
		}
		mqttClient.close();
		mqttClient = null;
		logger.debug("mqttClient가종료되었습니다.");
		logger.debug("destroy종료()");
	}

	/**
	 * @return
	 */
	public String getErrorMsg() throws Exception {
		logger.debug("getErrorMsg시작()");
		logger.debug("getErrorMsg종료(errorMsg=" + errorMsg + ")");
		return errorMsg;
	}

	@Override
	public synchronized void publish(Message msg) throws Exception {
		logger.debug("publish시작()");
		if (mqttClient == null || !mqttClient.isConnected()) {
			logger.debug("message전송실패");
			throw new PushException("메시지전송실패");
		}

		mqttClient.publish(msg.getReceiver(), msg.getMessage().getBytes(),
				msg.getQos(), msg.isRetained());
		logger.debug("publish종료()");
	}

	@Override
	public boolean isConnected() throws Exception {
		logger.debug("isConnected시작()");
		logger.debug("isConnected종료(" + mqttClient.isConnected() + ")");
		return mqttClient.isConnected();
	}

	/*
	 * 푸시서버 초당 받은 메시지 건수
	 * 
	 * @see kr.co.adflow.push.service.MqttService#getTps()
	 */
	@Override
	public double getTps() throws Exception {
		return tps;
	}

}
