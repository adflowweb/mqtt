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

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 7. 24.
 */
@Component
public class HAHandler implements Runnable {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(HAHandler.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(HAHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static boolean first = true; // thread name 세팅용

	private boolean ha = Boolean.parseBoolean(prop.getProperty("ha.enable"));

	private int haInterval = Integer.parseInt(prop
			.getProperty("ha.check.interval"));

	private ScheduledExecutorService HALooper;

	@Resource
	private HAService haService;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("HAHandler초기화시작()");
		logger.info("HA처리유무=" + ha);
		if (ha) {
			HALooper = Executors.newScheduledThreadPool(1);
			HALooper.scheduleWithFixedDelay(this, 0, haInterval,
					TimeUnit.SECONDS);
			logger.info("HALooper가시작되었습니다.");
		}
		logger.info("HAHandler초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		if (ha) {
			HALooper.shutdown();
			logger.info("HALooper가종료되었습니다.");
		}
		logger.info("cleanUp종료()");
	}

	@Override
	public void run() {
		logger.debug("HACheck시작()");
		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("HAPrecessing " + orgName);
			first = !first;
		}

		try {
			haService.check();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("HACheck종료()");
	}

}
