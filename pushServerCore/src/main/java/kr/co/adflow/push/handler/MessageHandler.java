package kr.co.adflow.push.handler;

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

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.mapper.MessageMapper;
import kr.co.adflow.push.service.HAService;
import kr.co.adflow.push.service.MqttService;

import org.apache.ibatis.session.SqlSession;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 메시지처리
 * 
 * @author nadir93
 * @date 2014. 6. 12.
 */
@Component
public class MessageHandler implements Runnable {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageHandler.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MessageHandler.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 메시지처리 유무
	private boolean msgProcess = Boolean.parseBoolean(prop
			.getProperty("message.enable"));
	// 메시지처리주기
	private int messageInterval = Integer.parseInt(prop
			.getProperty("message.process.interval"));

	private static boolean first = true;

	@Resource
	MqttService mqttService;

	@Resource
	HAService haService;

	@Autowired
	private SqlSession sqlSession;

	protected ScheduledExecutorService messageLooper;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("MessageHandler초기화시작()");

		logger.info("메시지처리유무=" + msgProcess);
		if (msgProcess) {
			messageLooper = Executors.newScheduledThreadPool(1);
			messageLooper.scheduleWithFixedDelay(this, messageInterval,
					messageInterval, TimeUnit.SECONDS);
			logger.info("메시지핸들러가시작되었습니다.");
		}
		logger.info("MessageHandler초기화종료()");
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
		logger.info("cleanUp종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (first) {
			final String orgName = Thread.currentThread().getName();
			Thread.currentThread().setName("MessagePrecessing " + orgName);
			first = !first;
		}

		// 이중화요건
		// 마스터이면 메시지 처리
		if (!haService.isActive()) {
			logger.debug("마스터가아닙니다.");
			return;
		}

		logger.debug("메시지처리시작()");

		try {
			String errMsg = mqttService.getErrorMsg();
			if (errMsg == null) {
				// mqtt connection이 정상일때만 처리함
				MessageMapper msgMapper = sqlSession
						.getMapper(MessageMapper.class);
				List<Message> list = (List<Message>) msgMapper
						.getUndeliveredMsgs();
				for (Message msg : list) {
					try {
						logger.debug("msg=" + msg);
						if (msg.getReservation() == null) {
							// 아이폰 안드로이드 구별하여야함
							// 아이폰일경우 전체 메시지 or 그룹메시지와 상관없이 apns로 전송해야함
							// 즉시전송메시지
							logger.debug("즉시전송대상입니다.발송을시작합니다.");
							publish(msgMapper, msg);
						} else {
							// 예약메시지
							logger.debug("예약전송대상입니다.");
							if (msg.getReservation().before(new Date())) {
								logger.debug("예약발송을시작합니다.");
								publish(msgMapper, msg);
							}
						}
					} catch (MqttException e) {
						e.printStackTrace();
						// 너무 많은 발행이 진행 중임 (32202)
						if (e.getReasonCode() != 32202) {
							// 메시지 처리중 장애발생
							msg.setStatus(e.getReasonCode());
							msgMapper.putStatus(msg);
							logger.error("메시지처리중에러발생.에러=" + e.getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
						// 메시지 처리중 장애발생
						msg.setStatus(Message.STATUS_ERROR);
						msgMapper.putStatus(msg);
						logger.error("메시지처리중에러발생.에러=" + e.getMessage());
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

	private void publish(MessageMapper msgMapper, Message msg) throws Exception {
		logger.debug("publish시작(msgMapper=" + msgMapper + ", msg=" + msg + ")");
		Message message = msgMapper.get(msg.getId());
		logger.debug("message(컨텐츠포함)=" + message);
		IMqttDeliveryToken token = mqttService.publish(message);

		// 토큰과 유저아이디를 hashmap에 저장한다

		logger.debug("메시지를전송하였습니다.token=" + token);
		// 모니터링용 송신 메시지 처리건수 계산 추가해야함

		// 전송후 db(issue) update
		msg.setIssue(new Date());
		msg.setStatus(Message.STATUS_PUSH_SENT);
		msgMapper.putIssue(msg);
		logger.debug("전송시간정보를업데이트했습니다.");
		logger.debug("publish종료()");
	}
}