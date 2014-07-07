package kr.co.adflow.push.bsbank.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.handler.SMSHandler;
import kr.co.adflow.push.dao.impl.DefaultMessageDaoImpl;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.handler.MessageHandler;
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
public class MessageDaoImpl extends DefaultMessageDaoImpl {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageDaoImpl.class);

	@Autowired
	private MessageHandler msgHandler;

	@Autowired
	private SMSHandler smsHandler;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("MessageDAOImpl초기화시작()");

		logger.info("메시지처리유무=" + msgProcess);
		if (msgProcess) {
			messageLooper = Executors.newScheduledThreadPool(1);
			messageLooper.scheduleWithFixedDelay(msgHandler, messageInterval,
					messageInterval, TimeUnit.SECONDS);
			logger.info("메시지핸들러가시작되었습니다.");
		}

		logger.info("SMS처리유무=" + sms);
		if (sms) {
			smsLooper = Executors.newScheduledThreadPool(1);
			smsLooper.scheduleWithFixedDelay(smsHandler, smsInterval,
					smsInterval, TimeUnit.SECONDS);
			logger.info("SMS핸들러가시작되었습니다.");
		}
		logger.info("MessageDAOImpl초기화종료()");
	}
}
