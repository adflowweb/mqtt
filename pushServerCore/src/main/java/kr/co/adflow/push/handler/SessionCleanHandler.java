/*
 * 
 */
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
import kr.co.adflow.push.service.UserService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
@Component
public class SessionCleanHandler implements Runnable {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SessionCleanHandler.class);

	/** The Constant CONFIG_PROPERTIES. */
	private static final String CONFIG_PROPERTIES = "/config.properties";

	/** The prop. */
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(SessionCleanHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String PROVISIONINGURL = prop.getProperty("pro.url");

	/** The first. */
	private static boolean first = true; // thread name 세팅용

	/** The sessionclean. */
	private boolean sessionclean = Boolean.parseBoolean(prop.getProperty("sc.enable"));

	/** The sessionclean interval. */
	private int sessioncleanInterval = Integer.parseInt(prop.getProperty("sc.check.interval"));

	private int lastAccessLimit = Integer.parseInt(prop.getProperty("sc.last.access.limit"));

	/** The SessonClean looper. */
	private ScheduledExecutorService SessonCleanLooper;

	/** The sessionclean service. */
	@Resource
	private UserService sessioncleanService;

	@Autowired
	private ZookeeperHandler zookeeperHandler;

	/**
	 * initialize.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("SessonCleanHandler초기화시작()");
		logger.info("SessonClean처리유무=" + sessionclean);
		if (sessionclean) {
			SessonCleanLooper = Executors.newScheduledThreadPool(1);
			SessonCleanLooper.scheduleWithFixedDelay(this, 0, 10, TimeUnit.SECONDS);
			logger.debug("session clean change..10!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.info("SessonCleanLooper가시작되었습니다.");

		}
		logger.info("SessonCleanHandler초기화종료()");
	}

	/**
	 * 모든리소스정리.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		if (sessionclean) {
			SessonCleanLooper.shutdown();

			logger.info("SessonCleanLooper가종료되었습니다.");
		}
		logger.info("cleanUp종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.debug("SessonCleanCheck시작()");
		// TODO:추후삭제
		logger.debug("리더인지 확인:");
		if (!zookeeperHandler.getLeader()) {
			logger.debug("리더가 아님");
			return;
		}
		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("SessonCleanPrecessing " + orgName);
			first = !first;
		}

		try {
			sessioncleanService.expiredSessionList(lastAccessLimit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("SessonCleanCheck종료()");
	}

}
