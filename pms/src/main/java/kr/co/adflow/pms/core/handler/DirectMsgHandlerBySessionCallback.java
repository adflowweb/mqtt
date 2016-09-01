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

public class DirectMsgHandlerBySessionCallback implements SessionCallback<String> {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DirectMsgHandlerBySessionCallback.class);

	private JmsTemplate jmsTemplate;
	/** The msg. */
	private Message msg;

	public DirectMsgHandlerBySessionCallback(JmsTemplate jmsTemplate, Message msg) {
		this.jmsTemplate = jmsTemplate;
		this.msg = msg;
	}

	@Override
	public String doInJms(Session session) throws JMSException {
		MessageProducer producer = null;

		logger.debug("=== msg::{}",
				"getContentType::" + msg.getContentType() + ",getExpiry::" + msg.getExpiry() + ",getQos::"
						+ msg.getQos() + ",getReceiver::" + msg.getReceiver() + ",getIssueId::" + msg.getIssueId()
						+ ",getAppAckType::" + msg.getAppAckType() + ",getGroupId::" + msg.getGroupId()
						+ ",getIssueName::" + msg.getIssueName() + ",getKeyMon::" + msg.getKeyMon() + ",getMediaType::"
						+ msg.getMediaType() + ",getMsgId::" + msg.getMsgId() + ",getMsgSize::" + msg.getMsgSize()
						+ ",getMsgType::" + msg.getMsgType() + ",getPmaAckType::" + msg.getPmaAckType()
						+ ",getReceiverTopic::" + msg.getReceiverTopic() + ",getResendId::" + msg.getResendId()
						+ ",getResendMaxCount::" + msg.getResendMaxCount() + ",getRetained::" + msg.getRetained()
						+ ",getSendTerminalType::" + msg.getSendTerminalType() + ",getServerId::" + msg.getServerId()
						+ ",getServiceId::" + msg.getServiceId() + ",getStatus::" + msg.getStatus() + ",getUpdateId::"
						+ msg.getUpdateId() + ",getAppAckTime::" + msg.getAppAckTime() + ",getPmaAckTime::"
						+ msg.getIssueTime() + ",getPmaAckTime::" + msg.getPmaAckTime() + ",getReservationTime::"
						+ msg.getReservationTime() + ",getUpdateTime::" + msg.getUpdateTime());
		String result = "";
		String json = "";
		byte[] byteArr = null;
		try {
			Destination destination = jmsTemplate.getDestinationResolver().resolveDestinationName(session,
					msg.getReceiverTopic(), true);
			producer = session.createProducer(destination);

			logger.debug("producer=" + producer);

			// producer.setTimeToLive(msg.getExpiry());
			// producer.setDeliveryMode(msg.getQos());

			JSONObject msgObject = new JSONObject();
			BytesMessage bytesMessage = session.createBytesMessage();

			msgObject.put("msgType", msg.getMsgType());
			msgObject.put("msgId", msg.getMsgId());
			msgObject.put("issueId", msg.getIssueId());
			msgObject.put("fileFormat", msg.getFileFormat());
			msgObject.put("fileName", msg.getFileName());
			msgObject.put("sender", msg.getIssueName());
			msgObject.put("receiver", msg.getReceiverTopic());
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

			if (result.equals("") || result == null) {
				throw new JMSException("테스트 예외 발생 코드");
			}
			producer.send(bytesMessage, msg.getQos(), javax.jms.Message.DEFAULT_PRIORITY/* default */, msg.getExpiry());
			result = "success";
			logger.debug("메시지가전송되었습니다.");
		} catch (JSONException e) {
			logger.error("메시지 처리도중 Json Exception 발생");
			logger.info("JSONException 의 경우 executor 에서는 처리 할 수 없으므로 예외처리후 종료");

		} catch (JMSException e) {
			logger.debug("테스트 상황!!!!!!!!!!");
			logger.error("JMSException:" + e);
			result = "fail";
			// 예외발생 테스트 코드
			throw new JMSException("테스트 예외");
			// return result;
		} finally {
			if (producer != null) {
				producer.close();
				logger.debug("메시지프로듀서가제거되었습니다.");
			}
		}
		return result;
	}

}
