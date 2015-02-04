package kr.co.adflow.pms.core.handler;

import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import kr.co.adflow.pms.domain.Ack;
import kr.co.adflow.pms.domain.mapper.AckMapper;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AckMessageDrivenBean implements MessageListener {

	private static final Logger logger = LoggerFactory
			.getLogger(AckMessageDrivenBean.class);

	@Autowired
	private AckMapper ackMapper;

	public void onMessage(Message message) {

		logger.info("Message Driven Bean: New Message");
		byte[] body = null;
		try {
			body = new byte[(int) ((BytesMessage) message).getBodyLength()];
			((BytesMessage) message).readBytes(body);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int cnt = ackMapper.insertAck(this.getAck(body));
		logger.info("ack result : {}", cnt);

	}

	private Ack getAck(byte[] body) {
		Ack ack = new Ack();

		String text = new String(body);
		JSONObject msgObject = new JSONObject(text);
		
		ack.setKeyMon(this.getKeyMon(msgObject.getString("msgID")));
		ack.setMsgId(msgObject.getString("msgID"));
		ack.setAckType(msgObject.getString("ackType"));
		ack.setTokenId(msgObject.getString("token"));
		ack.setAckTime(new Date(msgObject.getLong("ackTime")));

		return ack;

	}

	private String getKeyMon(String string) {
		String result = string.substring(0, 6);
		logger.debug("getKeyMon is {}",result);
		return result;
	}

}
