package kr.co.adflow.push.service.impl;

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
import kr.co.adflow.push.service.ConnectionService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Service
public class MqttConnectionServiceImpl implements Runnable, MqttCallback,
		ConnectionService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MqttConnectionServiceImpl.class);

	public static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	private static final String TOPIC = "testTopic";
	private static final String SERVERURL = "tcp://adflow.net:1883";
	private static final String DEVICEID = "ADFlowPushServer";
	private ScheduledExecutorService scheduledExecutorService;
	private MqttClient mqttClient;
	private int healthCheckInterval = 5; // second
	private int connectionTimeout = 3; // second
	private int keepAliveInterval = 2; // second
	private String errorMsg;

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("mqtt커넥션서비스를시작합니다.");
		setMqttClientLog();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleWithFixedDelay(this, 0,
				healthCheckInterval, TimeUnit.SECONDS);
	}

	/**
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		destroy();
		scheduledExecutorService.shutdown();
		logger.info("mqtt커넥션서비스를종료합니다.");
	}

	private void setMqttClientLog() {
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger rootLogger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(Level.SEVERE);
		rootLogger.setLevel(Level.SEVERE);
		rootLogger.addHandler(defaultHandler);
		logManager.addLogger(rootLogger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.debug("mqtt헬스체크를시작합니다.");
		try {
			if (mqttClient == null) {
				logger.debug("mqtt연결을처음시도합니다.");
				connectAndSubscribe();
			} else if (!mqttClient.isConnected()) {
				logger.debug("서버와연결되어있지않으므로접속을시도합니다.");
				reconnect();
			}
			errorMsg = null;
		} catch (Exception e) {
			errorMsg = e.toString();
			logger.error("문제가발생하였습니다.", e);
		}

		logger.debug("mqtt헬스체크를종료합니다.");
	}

	private void reconnect() throws MqttException {
		mqttClient.connect();
		// 리커넥트시에 초기시도했던 상태값들이 정상적으로 되어있는지
		// 커넥트옵션이나 서브스크라이브가 정확히 되어있는지 상태 체크바람
		logger.debug("세션연결및토픽구독?(구독연부확인바람)을완료하였습니다.");
	}

	/**
	 * @throws MqttException
	 */
	protected void connectAndSubscribe() throws MqttException {
		MqttConnectOptions mOpts = new MqttConnectOptions();
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(false);
		mqttClient = new MqttClient(SERVERURL, DEVICEID);
		logger.debug("mqttClient인스턴스가생성되었습니다.::" + mqttClient);
		mqttClient.setCallback(this);
		mqttClient.connect(mOpts);
		mqttClient.subscribe(TOPIC, 2);
		logger.debug("세션연결및토픽구독을완료하였습니다.");
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
		errorMsg = throwable.toString();
		logger.error("mqttTCP세션연결이끊겼습니다.", throwable);
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
		logger.info("deliveryComplete::" + token);
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
		logger.info("messageArrived::topic::" + topic + "::message::" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * mqttClient를 종료하는 메소드 mqttClient가 재활용되기때문에 최후에 정리하고 종료되어야합니다.
	 * 
	 * @see kr.co.adflow.push.service.ConnectionService#destroy()
	 */
	public void destroy() throws Exception {
		logger.debug("mqttClient::" + mqttClient);
		if (mqttClient == null) {
			return;
		}

		logger.debug("mqttClient세션연결상태::" + mqttClient.isConnected());
		if (mqttClient.isConnected()) {
			mqttClient.disconnect();
			logger.debug("mqttClient연결을끊었습니다.");
		}
		mqttClient.close();
		logger.debug("mqttClient가종료되었습니다.");
	}

	/**
	 * @return
	 */
	public String getErrorMsg() throws Exception {
		return errorMsg;
	}

	@Override
	public boolean publish(Message msg) throws Exception {

		if (mqttClient == null || !mqttClient.isConnected()) {
			return false;
		}

		MqttMessage message = new MqttMessage(msg.getMessage().getBytes());
		message.setQos(2);
		MqttTopic topic = mqttClient.getTopic(TOPIC);
		topic.publish(message);

		return true;
	}

	@Override
	public boolean isConnected() throws Exception {
		return mqttClient.isConnected();
	}
}
