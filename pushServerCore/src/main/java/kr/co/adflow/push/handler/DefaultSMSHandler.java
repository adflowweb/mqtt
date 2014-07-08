package kr.co.adflow.push.handler;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.LoggerFactory;

/**
 * @author nadir93
 * @date 2014. 7. 8.
 * 
 */
public abstract class DefaultSMSHandler implements Runnable {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DefaultSMSHandler.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(DefaultSMSHandler.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected ScheduledExecutorService smsLooper;

	// SMS처리 유무
	protected boolean sms = Boolean
			.parseBoolean(prop.getProperty("sms.enable"));
	// sms처리주기
	protected int smsInterval = Integer.parseInt(prop
			.getProperty("sms.process.interval"));

	private static boolean first = true; // thread name 세팅용

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("DefaultSMSHandler초기화시작()");
		logger.info("SMS처리유무=" + sms);
		if (sms) {
			smsLooper = Executors.newScheduledThreadPool(1);
			smsLooper.scheduleWithFixedDelay(this, smsInterval, smsInterval,
					TimeUnit.SECONDS);
			logger.info("SMS핸들러가시작되었습니다.");
		}
		logger.info("DefaultSMSHandler초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		if (sms) {
			smsLooper.shutdown();
			logger.info("SMS핸들러가종료되었습니다.");
		}
		logger.info("cleanUp종료()");
	}

	@Override
	public void run() {
		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("SMSPrecessing " + orgName);
			first = !first;
		}
		doProcess();
	}

	protected abstract void doProcess();

}
