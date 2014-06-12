package kr.co.adflow.push.dao.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDAO;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.mapper.MessageMapper;
import kr.co.adflow.push.service.MqttService;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component
public class MessageDAOImpl implements MessageDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageDAOImpl.class);

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

	@Resource
	MqttService mqttService;

	@Autowired
	private SqlSession sqlSession;

	private ScheduledExecutorService smsLooper;
	private ScheduledExecutorService reservationLooper;

	private int reservationInterval = Integer.parseInt(prop
			.getProperty("reservation.process.interval"));
	private int smsInterval = Integer.parseInt(prop
			.getProperty("sms.process.interval"));

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("MessageDAOImpl초기화시작()");

		reservationLooper = Executors.newScheduledThreadPool(1);
		reservationLooper.scheduleWithFixedDelay(new ReservationHandler(), 60,
				reservationInterval, TimeUnit.SECONDS);
		smsLooper = Executors.newScheduledThreadPool(1);
		smsLooper.scheduleWithFixedDelay(new SMSHandler(), 60, smsInterval,
				TimeUnit.SECONDS);
		logger.info("예약&SMSLooper가시작되었습니다.");
		logger.info("MessageDAOImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		reservationLooper.shutdown();
		smsLooper.shutdown();
		logger.info("예약&SMSLooper가종료되었습니다.");
		logger.info("cleanUp종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#get(java.lang.String)
	 */
	@Override
	public Message get(int messageID) throws Exception {
		logger.debug("get시작(messageID=" + messageID + ")");

		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message msg = msgMapper.get(messageID);

		logger.debug("get종료(" + msg + ")");
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#post(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public void post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");

		// 트랜잭션 처리 필요함

		// 예약전송이 아닐경우 바로 전송
		if (msg.getReservation() == null) {
			// 전송
			mqttService.publish(msg);
			// 전송시간세팅
			msg.setIssue(new Date());
		}

		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		msgMapper.post(msg);
		msg.setId(msgMapper.getID(msg));
		logger.debug("msg=" + msg);
		msgMapper.postContent(msg);

		logger.debug("post종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public void put(Message msg) throws Exception {
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public void delete(int messageID) throws Exception {
		// return null;
	}

	/**
	 * 예약메시지처리
	 * 
	 * @author nadir93
	 * @date 2014. 6. 12.
	 */
	class ReservationHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("예약메시지처리시작()");

			try {
				if (mqttService.getErrorMsg() == null) {
					// mqtt connection이 정상일때만 처리함
					MessageMapper msgMapper = sqlSession
							.getMapper(MessageMapper.class);
					List<Message> list = (List<Message>) msgMapper
							.getReservation();
					for (Message msg : list) {
						logger.debug("msg=" + msg);
						// 시간이 맞으면 전송
						if (msg.getReservation().before(new Date())) {
							logger.debug("전송대상입니다.");
							Message message = msgMapper.get(msg.getId());
							mqttService.publish(message);
							// 전송후 db(issue) update
							msg.setIssue(new Date());
							msgMapper.put(msg);
						}

					}
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
			}

			// msgMapper.post(msg);
			// msg.setId(msgMapper.getID(msg));
			// logger.debug("msg=" + msg);
			// msgMapper.postContent(msg);

			logger.debug("예약메시지처리종료()");
		}

	}

	/**
	 * sms메시지처리
	 * 
	 * @author nadir93
	 * @date 2014. 6. 12.
	 */
	class SMSHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("SMS메시지처리시작()");
			logger.debug("SMS메시지처리종료()");
		}
	}
}
