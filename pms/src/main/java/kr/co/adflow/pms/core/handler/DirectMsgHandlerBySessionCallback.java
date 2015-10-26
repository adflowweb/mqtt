package kr.co.adflow.pms.core.handler;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.MessageRunTimeException;
import kr.co.adflow.pms.domain.Message;

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

		logger.debug(msg.toString());

		String json = "";
		byte[] byteArr = null;
		try {
			Destination destination = jmsTemplate.getDestinationResolver()
					.resolveDestinationName(session, msg.getReceiver(), true);
			producer = session.createProducer(destination);

			logger.debug("producer=" + producer);
			JSONObject msgObject = new JSONObject();
			BytesMessage bytesMessage = session.createBytesMessage();

			msgObject.put("msgType", msg.getMsgType());
			msgObject.put("msgId", msg.getMsgId());
			// msgObject.put("issueId", msg.getIssueId());
			msgObject.put("sender", msg.getIssueId());
			msgObject.put("receiver", msg.getReceiver());
			msgObject.put("issueTime", msg.getIssueTime());
			if (msg.isAck()) {
				msgObject.put("ack", 1);
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

			logger.debug("=== json::{}", json);

			byteArr = json.getBytes();
			bytesMessage.writeBytes(byteArr);

			if (msg.getQos() <= 0) {
				// Qos 0 => delivery mod 1
				msg.setQos(1);
			} else {
				// Qos 1,2 => delivery mod 2
				msg.setQos(2);
			}

			producer.send(bytesMessage, msg.getQos(),
					javax.jms.Message.DEFAULT_PRIORITY/* default */,
					msg.getExpiry());
			logger.debug("메시지가전송되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_539000, "메시지 전송에 실패 하였습니다");
			} catch (MessageRunTimeException ms) {
				// TODO Auto-generated catch block
				ms.printStackTrace();
			}

		} finally {
			if (producer != null) {
				producer.close();
				logger.debug("메시지프로듀서가제거되었습니다.");
			}
		}
		return "";
	}

}
