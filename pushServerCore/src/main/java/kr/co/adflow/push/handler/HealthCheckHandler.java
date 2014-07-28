package kr.co.adflow.push.handler;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.service.HAService;
import kr.co.adflow.push.service.MqttService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 헬스체크 핸들러
 * 
 * @author nadir93
 * @date 2014. 7. 7.
 * 
 */
@Component
public class HealthCheckHandler implements Runnable {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(HealthCheckHandler.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(HealthCheckHandler.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean first = true; // thread name 세팅용

	private boolean healthCheck = Boolean.parseBoolean(prop
			.getProperty("health.enable"));

	private int healthCheckInterval = Integer.parseInt(prop
			.getProperty("health.check.interval"));

	private ScheduledExecutorService healthCheckLooper;

	@Resource
	private MqttService mqttService;

	@Resource
	HAService haService;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("HealthCheckHandler초기화시작()");
		logger.info("헬스체크처리유무=" + healthCheck);
		if (healthCheck) {
			healthCheckLooper = Executors.newScheduledThreadPool(1);
			healthCheckLooper.scheduleWithFixedDelay(this, 0,
					healthCheckInterval, TimeUnit.SECONDS);
			logger.info("healthCheckLooper가시작되었습니다.");
		}
		logger.info("HealthCheckHandler초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		if (healthCheck) {
			healthCheckLooper.shutdown();
			logger.info("healthCheckLooper가종료되었습니다.");
		}
		logger.info("cleanUp종료()");
	}

	@Override
	public void run() {
		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("HealthCheckPrecessing " + orgName);
			first = !first;
		}

		// 이중화요건
		// 마스터이면 헬스체크
		if (!haService.isActive()) {
			logger.debug("마스터가아닙니다.");
			// check mqttClient& disconnect
			//
			return;
		}

		logger.debug("healthCheck시작()");
		try {
			// mqtt connection 헬스체크
			mqttService.healthCheck();
			// 모니터링용 수신 메시지 처리건수 계산
			mqttService.generateTPS();
		} catch (Exception e) {
			mqttService.setError(e.toString());
			logger.error("에러발생", e);
		}
		logger.debug("healthCheck종료()");
	}
}
