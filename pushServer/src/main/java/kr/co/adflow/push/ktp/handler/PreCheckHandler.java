package kr.co.adflow.push.ktp.handler;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.push.ktp.service.impl.PlatformServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.ProducerCallback;

public class PreCheckHandler implements ProducerCallback<Object> {
	
	final static private int TIME_TO_LIVE = 3000;
	
	final static private String PRECHECK_MESSAGE = "{\"type\":103}";
	
//	public PreCheckHandler(String msg) {
//		msg = test;
//	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(PreCheckHandler.class);

	@Override
	public Object doInJms(Session session, MessageProducer producer) throws JMSException {

		producer.setPriority(6);
		producer.setTimeToLive(TIME_TO_LIVE);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		
		BytesMessage bytesMessage = session.createBytesMessage();
		//String testMessage = "{type:103}";
		String testMessage = PRECHECK_MESSAGE;
		byte[] byteArr = new byte[1024];
		byteArr = testMessage.getBytes();
		bytesMessage.writeBytes(byteArr);
		
		logger.info("testMessage=" + testMessage);
		System.out.println("testMessage=" + testMessage);

		producer.send(bytesMessage);
		producer.close();
		
		System.out.println("end:::=");
		return byteArr;
	}

}
