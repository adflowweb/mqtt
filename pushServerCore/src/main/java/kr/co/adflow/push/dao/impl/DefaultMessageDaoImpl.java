package kr.co.adflow.push.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.dao.SMSDao;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.mapper.MessageMapper;
import kr.co.adflow.push.service.MqttService;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
// @Component
public class DefaultMessageDaoImpl implements MessageDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DefaultMessageDaoImpl.class);

	private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(DefaultMessageDaoImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Resource
	MqttService mqttService;

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private SMSDao smsDao;

	protected ScheduledExecutorService smsLooper;
	protected ScheduledExecutorService messageLooper;

	// 메시지처리 유무
	protected boolean msgProcess = Boolean.parseBoolean(prop
			.getProperty("message.enable"));
	// 메시지처리주기
	protected int messageInterval = Integer.parseInt(prop
			.getProperty("message.process.interval"));
	// SMS처리 유무
	protected boolean sms = Boolean
			.parseBoolean(prop.getProperty("sms.enable"));
	// sms처리주기
	protected int smsInterval = Integer.parseInt(prop
			.getProperty("sms.process.interval"));

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("MessageDAOImpl초기화시작()");

		// logger.info("메시지처리유무=" + msgProcess);
		// if (msgProcess) {
		// messageLooper = Executors.newScheduledThreadPool(1);
		// messageLooper.scheduleWithFixedDelay(new MessageHandler(),
		// messageInterval, messageInterval, TimeUnit.SECONDS);
		// logger.info("메시지핸들러가시작되었습니다.");
		// }
		//
		// logger.info("SMS처리유무=" + sms);
		// if (sms) {
		// smsLooper = Executors.newScheduledThreadPool(1);
		// smsLooper.scheduleWithFixedDelay(new SMSHandler(), smsInterval,
		// smsInterval, TimeUnit.SECONDS);
		// logger.info("SMS핸들러가시작되었습니다.");
		// }
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

		if (msgProcess) {
			messageLooper.shutdown();
			logger.info("메시지핸들러가종료되었습니다.");
		}

		if (sms) {
			smsLooper.shutdown();
			logger.info("SMS핸들러가종료되었습니다.");
		}

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

	@Override
	public Message[] getReservationMsgs() throws Exception {
		logger.debug("getReservationMsgs시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message[] msg = msgMapper.getReservationMsgs();
		logger.debug("getReservationMsgs종료(" + msg + ")");
		return msg;
	}

	@Override
	public Message[] getDeliveredMsgs() throws Exception {
		logger.debug("getDeliveredMsgs시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message[] msg = msgMapper.getDeliveredMsgs();
		logger.debug("getDeliveredMsgs종료(" + msg + ")");
		return msg;
	}
}
