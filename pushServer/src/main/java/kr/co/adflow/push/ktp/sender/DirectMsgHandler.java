/*
 * 
 */
package kr.co.adflow.push.ktp.sender;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.push.domain.Message;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.ProducerCallback;

// TODO: Auto-generated Javadoc
/**
 * The Class DirectMsgHandler.
 */
public class DirectMsgHandler implements ProducerCallback<Object> {

	/** The msg. */
	private Message msg;

	/**
	 * Instantiates a new direct msg handler.
	 * 
	 * @param message
	 *            the message
	 */
	public DirectMsgHandler(Message message) {
		msg = message;
	}

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(DirectMsgHandler.class);

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

		producer.setTimeToLive(msg.getExpiry());
		producer.setDeliveryMode(msg.getQos());

		String json = "";
		byte[] byteArr = null;
		try {
			JSONObject msgObject = new JSONObject();
			BytesMessage bytesMessage = session.createBytesMessage();

			msgObject.put("msgType", msg.getType());
			msgObject.put("msgId", msg.getId());
			msgObject.put("sender", msg.getSender());
			msgObject.put("receiver", msg.getReceiver());
			msgObject.put("contentType", msg.getContentType());
			msgObject.put("serviceId", msg.getServiceID());
			if (msg.isAck()) {
				msgObject.put("ack", 1);
			}

			if ("application/json".equals(msg.getContentType())) {
				JSONObject contentObject = new JSONObject(msg.getContent());
				msgObject.put("content", contentObject);
			} else {
				msgObject.put("content", msg.getContent());
			}

			json = msgObject.toString();
			logger.info("json::{}", json);

			byteArr = json.getBytes();
			bytesMessage.writeBytes(byteArr);

			producer.send(bytesMessage);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
