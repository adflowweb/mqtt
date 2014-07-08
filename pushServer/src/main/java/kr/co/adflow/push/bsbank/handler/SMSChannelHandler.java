package kr.co.adflow.push.bsbank.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.adflow.push.bsbank.sms.SMSSender;
import kr.co.adflow.push.dao.SMSDao;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nadir93
 * @date 2014. 7. 8.
 * 
 */
public class SMSChannelHandler implements Runnable {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SMSChannelHandler.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(SMSChannelHandler.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int sendChannelInterval = Integer.parseInt(prop
			.getProperty("sendChannel.process.interval"));

	// private int recvChannelInterval = Integer.parseInt(prop
	// .getProperty("recvChannel.process.interval"));

	private String smsServer = prop.getProperty("sms.server.url");

	private int smsPort = Integer.parseInt(prop.getProperty("sms.server.port"));

	private boolean sms = Boolean.parseBoolean(prop.getProperty("sms.enable"));

	private final String SERVER_IP = smsServer;
	private final int PORT = smsPort;

	private ScheduledExecutorService sendChannel;

	@Autowired
	private SMSDao<SMSSender> smsDao;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("SMSDAOImpl초기화시작()");
		if (sms) {
			sendChannel = Executors.newScheduledThreadPool(1);
			sendChannel.scheduleWithFixedDelay(this, sendChannelInterval,
					sendChannelInterval, TimeUnit.SECONDS);
			logger.info("sendChannel핸들러가시작되었습니다.");
		}
		logger.info("SMSDAOImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		try {
			logger.info("cleanUp시작()");
			if (sms) {
				sendChannel.shutdown();
				logger.info("sendChannel핸들러가종료되었습니다.");
			}
			logger.info("cleanUp종료()");
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
	}

	@Override
	public void run() {
		logger.debug("sendChannel처리시작()");
		logger.debug("sender=" + smsDao.getSMSSender());
		try {
			if (smsDao.getSMSSender() == null) {
				initSender();
			}
			smsDao.getSMSSender().sendPING();
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
		logger.debug("sendChannel처리종료()");
	}

	/**
	 * socket blocking
	 * 
	 * @throws Exception
	 */
	private void initSender() throws Exception {
		logger.debug("initSender시작()");
		if (smsDao.getSMSSender() != null) {
			logger.debug("sender가널값이아닙니다.");
			return;
		}
		logger.debug("sendSocket연결시작 서버주소=" + SERVER_IP + ", 포트=" + PORT);
		logger.debug("소케연결기다림..(블락킹)");
		Socket socket = new Socket(SERVER_IP, PORT);
		logger.debug("소켓이연결되었습니다.socket=" + socket);
		smsDao.setSMSSender(new SMSSender(this, socket));
		smsDao.getSMSSender().connect();
		// sender.start();
		logger.debug("샌더가시작되었습니다. sender=" + smsDao.getSMSSender());
		logger.debug("initSender종료()");
	}

	public void setSMSSender(SMSSender sender) {
		smsDao.setSMSSender(sender);
	}
}
