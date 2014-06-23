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
	private ScheduledExecutorService messageLooper;

	private int messageInterval = Integer.parseInt(prop
			.getProperty("message.process.interval"));
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
		messageLooper = Executors.newScheduledThreadPool(1);
		messageLooper.scheduleWithFixedDelay(new MessageHandler(),
				messageInterval, messageInterval, TimeUnit.SECONDS);
		logger.info("메시지핸들러가시작되었습니다.");
		smsLooper = Executors.newScheduledThreadPool(1);
		smsLooper.scheduleWithFixedDelay(new SMSHandler(), smsInterval,
				smsInterval, TimeUnit.SECONDS);
		logger.info("SMS핸들러가시작되었습니다.");
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
		messageLooper.shutdown();
		logger.info("메시지핸들러가종료되었습니다.");
		smsLooper.shutdown();
		logger.info("SMS핸들러가종료되었습니다.");
		logger.info("cleanUp종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#get(java.lang.String)
	 */
	@Override
	public Message get(int msgID) throws Exception {
		logger.debug("get시작(msgID=" + msgID + ")");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message msg = msgMapper.get(msgID);
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
	public int post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.postMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.postContent(msg);
		logger.debug("post종료(count=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public int put(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.putMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.putContent(msg);
		logger.debug("put종료(count=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public int delete(int msgID) throws Exception {
		logger.debug("delete시작(msgID=" + msgID + ")");
		MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
		int result = messageMapper.deleteMsg(msgID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/**
	 * 메시지처리
	 * 
	 * @author nadir93
	 * @date 2014. 6. 12.
	 */
	class MessageHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("메시지처리시작()");

			try {
				String errMsg = mqttService.getErrorMsg();
				if (errMsg == null) {
					// mqtt connection이 정상일때만 처리함
					MessageMapper msgMapper = sqlSession
							.getMapper(MessageMapper.class);
					List<Message> list = (List<Message>) msgMapper
							.getUndeliveredMsg();
					for (Message msg : list) {
						logger.debug("msg=" + msg);
						if (msg.getReservation() == null) {
							// 즉시전송메시지
							logger.debug("즉시전송대상입니다.");
							publish(msgMapper, msg);
						} else {
							// 예약메시지
							if (msg.getReservation().before(new Date())) {
								logger.debug("예약전송대상입니다.");
								publish(msgMapper, msg);
							}
						}
					}
				} else {
					logger.error("mqtt서비스에문제가있어메시지처리를skip합니다.errMsg=" + errMsg);
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
			logger.debug("메시지처리종료()");
		}

		private void publish(MessageMapper msgMapper, Message msg)
				throws Exception {
			Message message = msgMapper.get(msg.getId());
			mqttService.publish(message);
			logger.debug("메시지를전송하였습니다.");
			// 전송후 db(issue) update
			msg.setIssue(new Date());
			msgMapper.putIssue(msg);
			logger.debug("전송시간정보를업데이트했습니다.");
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
			logger.debug("SMS핸들러처리시작()");
			// db query select sms
			// send sms
			// db update
			logger.debug("SMS핸들러처리종료()");
		}
	}
}
