package kr.co.adflow.push.ktp.sender;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;

public class PreCheckHandlerBySessionCallback implements
		SessionCallback<String> {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PreCheckHandlerBySessionCallback.class);

	final static private String PRECHECK_MESSAGE = "{\"msgType\":103}";

	private JmsTemplate jmsTemplate;
	private String topicName;
	private int timeToLive;

	public PreCheckHandlerBySessionCallback(JmsTemplate jmsTemplate,
			String topicName, int timeToLive) {
		this.jmsTemplate = jmsTemplate;
		this.topicName = topicName;
		this.timeToLive = timeToLive;
	}

	@Override
	public String doInJms(Session session) throws JMSException {

		MessageProducer producer = null;

		try {
			Destination destination = jmsTemplate.getDestinationResolver()
					.resolveDestinationName(session, topicName, true);
			// MessageProducer producer = createProducer(session, destination);
			producer = session.createProducer(destination);
			logger.debug("producer=" + producer);

			// producer.setPriority(6);
			// producer.setTimeToLive(TIME_TO_LIVE);
			// producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			BytesMessage bytesMessage = session.createBytesMessage();
			String testMessage = PRECHECK_MESSAGE;
			byte[] byteArr = null;
			byteArr = testMessage.getBytes();
			bytesMessage.writeBytes(byteArr);

			// logger.info("testMessage=" + testMessage);
			// producer.send(bytesMessage);

			producer.send(bytesMessage, DeliveryMode.NON_PERSISTENT, 6,
					timeToLive);
			logger.debug("메시지가전송되었습니다.");
		} finally {
			if (producer != null) {
				producer.close();
				logger.debug("메시지프로듀서가제거되었습니다.");
			}
		}
		return PRECHECK_MESSAGE;
	}
}
