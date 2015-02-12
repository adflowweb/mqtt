/*
 * 
 */
package kr.co.adflow.pms.core.handler;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.pms.domain.Message;

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

		// jms priority default 4
		// producer.setPriority(4);
		producer.setTimeToLive(msg.getExpiry());
		producer.setDeliveryMode(msg.getQos());

		BytesMessage bytesMessage = session.createBytesMessage();
		String json = null;

		JSONObject msgObject = new JSONObject();
		try {

			msgObject.put("msgType", msg.getMsgType());
			msgObject.put("msgId", msg.getMsgId());
			msgObject.put("sender", msg.getIssueId());
			msgObject.put("receiver", msg.getReceiver());
			if (msg.isAck()) {
				msgObject.put("ack", 1);
			} else {
				msgObject.put("ack", 0);
			}
			
			msgObject.put("contentType", msg.getContentType());
			msgObject.put("serviceId", msg.getServiceId());
			
//			msgObject.put("type", msg.getMsgType());
//			msgObject.put("id", msg.getMsgId());
//			msgObject.put("sender", msg.getIssueId());
//			msgObject.put("receiver", msg.getReceiver());
//			msgObject.put("ack", msg.isAck());
//			msgObject.put("contentType", msg.getContentType());
//			msgObject.put("serviceID", msg.getServiceId());

			if ("application/json".equals(msg.getContentType())) {
				JSONObject contentObject = new JSONObject(msg.getContent());
				msgObject.put("content", contentObject);
			} else {
				msgObject.put("content", msg.getContent());
			}

			json = msgObject.toString();
			logger.info("json::{}", json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		byte[] byteArr = new byte[1024];

		byteArr = json.getBytes();
		bytesMessage.writeBytes(byteArr);

		producer.send(bytesMessage);
		producer.close();

		return byteArr;
	}

}
