package kr.co.adflow.pms.core.handler;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.pms.domain.Message;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;

public class DirectMsgHandlerBySessionCallback implements
		SessionCallback<String> {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(DirectMsgHandlerBySessionCallback.class);

	private JmsTemplate jmsTemplate;
	/** The msg. */
	private Message msg;

	public DirectMsgHandlerBySessionCallback(JmsTemplate jmsTemplate,
			Message msg) {
		this.jmsTemplate = jmsTemplate;
		this.msg = msg;
	}

	@Override
	public String doInJms(Session session) throws JMSException {
		MessageProducer producer = null;

		
		String json = "";
		byte[] byteArr = null;
		try {
			Destination destination = jmsTemplate.getDestinationResolver()
					.resolveDestinationName(session, msg.getReceiver(), true);
			producer = session.createProducer(destination);
			logger.debug("producer=" + producer);
			
//			producer.setTimeToLive(msg.getExpiry());
//			producer.setDeliveryMode(msg.getQos());

			JSONObject msgObject = new JSONObject();
			BytesMessage bytesMessage = session.createBytesMessage();
			
			msgObject.put("msgType", msg.getMsgType());
			msgObject.put("msgId", msg.getMsgId());
			//msgObject.put("sender", msg.getIssueId());
			msgObject.put("sender", msg.getIssueName());
			msgObject.put("receiver", msg.getReceiver());
			if (msg.isAck()) {
				msgObject.put("ack", 1);
			}
			// else {
			// msgObject.put("ack", 0);
			// }
			if (msg.getResendCount() != 0) {
				msgObject.put("resendCount", msg.getResendCount());
			}

			msgObject.put("contentType", msg.getContentType());
			msgObject.put("serviceId", msg.getServiceId());

			if ("application/json".equals(msg.getContentType())) {
				JSONObject contentObject = new JSONObject(msg.getContent());
				msgObject.put("content", contentObject);
			} else {
				msgObject.put("content", msg.getContent());
			}

			json = msgObject.toString();
			

			byteArr = json.getBytes();
			bytesMessage.writeBytes(byteArr);

			producer.send(bytesMessage, msg.getQos(),
					javax.jms.Message.DEFAULT_PRIORITY/* default */,
					msg.getExpiry());
			logger.debug("메시지가전송되었습니다.");
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (producer != null) {
				producer.close();
				logger.debug("메시지프로듀서가제거되었습니다.");
			}
		}
		return "";
	}

}
