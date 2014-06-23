package kr.co.adflow.push.dao.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.adflow.push.dao.SMSDAO;

import org.slf4j.LoggerFactory;

/**
 * @author nadir93
 * @date 2014. 6. 23.
 */
public class SMSDAOImpl implements SMSDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SMSDAOImpl.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MessageDAOImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int sendChannelInterval = Integer.parseInt(prop
			.getProperty("sendChannel.process.interval"));
	private int recvChannelInterval = Integer.parseInt(prop
			.getProperty("recvChannel.process.interval"));

	private ScheduledExecutorService sendChannel;
	private ScheduledExecutorService recvChannel;

	private ServerSocket server;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("SMSDAOImpl초기화시작()");
		sendChannel = Executors.newScheduledThreadPool(1);
		sendChannel.scheduleWithFixedDelay(new SendChannelHandler(),
				sendChannelInterval, sendChannelInterval, TimeUnit.SECONDS);
		logger.info("sendChannel핸들러가시작되었습니다.");
		recvChannel = Executors.newScheduledThreadPool(1);
		recvChannel.scheduleWithFixedDelay(new RecvChannelHandler(),
				recvChannelInterval, recvChannelInterval, TimeUnit.SECONDS);
		logger.info("recvChannel핸들러가시작되었습니다.");
		logger.info("SMSDAOImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		sendChannel.shutdown();
		recvChannel.shutdown();
		logger.info("cleanUp종료()");
	}

	/*
	 * sms message 전송
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.SMSDAO#post()
	 */
	@Override
	public void post() throws Exception {
		// TODO Auto-generated method stub

	}

	class SendChannelHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("sendChannel처리시작()");
			// if 연결되어 있지않다면
			// // resource  해제
			// // socket 연결
			// // send bind data
			// // receive bind ack
			// if 연결되어 있다면
			// // 헬스체크
			logger.debug("sendChannel처리종료()");
		}
	}

	class RecvChannelHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("recvChannel처리시작()");
			// if 연결되어 있지않다면
			// // resource  해제
			// // socket 연결
			// // send bind data
			// // receive bind ack
			// if 연결되어 있다면
			// // 헬스체크
			try {
				server = new ServerSocket(3000);
				Socket socket = server.accept();
			} catch (Exception e) {
				logger.error("에러발생", e);
			}

			logger.debug("recvChannel처리종료()");
		}
	}

}
