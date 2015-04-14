/*
 * 
 */
package kr.co.adflow.push.ktp.sender;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.ProducerCallback;

// TODO: Auto-generated Javadoc
/**
 * The Class PreCheckHandler.
 */
public class PreCheckHandler implements ProducerCallback<Object> {

	// final static private int TIME_TO_LIVE = 3000;

	/** The Constant PRECHECK_MESSAGE. */
	final static private String PRECHECK_MESSAGE = "{\"msgType\":103}";

	/** The time to live. */
	private int TIME_TO_LIVE;

	/**
	 * Instantiates a new pre check handler.
	 * 
	 * @param timeout
	 *            the timeout
	 */
	public PreCheckHandler(int timeout) {
		TIME_TO_LIVE = timeout;
	}

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PreCheckHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jms.core.ProducerCallback#doInJms(javax.jms.Session,
	 * javax.jms.MessageProducer)
	 */
	@Override
	public Object doInJms(Session session, MessageProducer producer)
			throws JMSException {
		byte[] byteArr = null;
		try {
			logger.info("TIME_TO_LIVE==============================={}",
					TIME_TO_LIVE);
			producer.setPriority(6);
			producer.setTimeToLive(TIME_TO_LIVE);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			BytesMessage bytesMessage = session.createBytesMessage();
			String testMessage = PRECHECK_MESSAGE;

			byteArr = testMessage.getBytes();
			bytesMessage.writeBytes(byteArr);

			logger.info("testMessage=" + testMessage);

			producer.send(bytesMessage);


		} finally {
			if (producer != null) {
				try {
					producer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return byteArr;
	}

}
