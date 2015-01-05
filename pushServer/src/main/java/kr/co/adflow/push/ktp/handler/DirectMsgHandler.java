package kr.co.adflow.push.ktp.handler;

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.ktp.service.impl.PlatformServiceImpl;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.ProducerCallback;

public class DirectMsgHandler implements ProducerCallback<Object> {
	
	private Message msg;
	
	public DirectMsgHandler(Message message) {
		msg = message;
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(DirectMsgHandler.class);

	@Override
	public Object doInJms(Session session, MessageProducer producer) throws JMSException {

		producer.setPriority(6);
		producer.setTimeToLive(msg.getTimeOut());
		producer.setDeliveryMode(msg.getQos());
		
		BytesMessage bytesMessage = session.createBytesMessage();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(msg);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] byteArr = new byte[1024];
	
		byteArr = json.getBytes();
		bytesMessage.writeBytes(byteArr);
		
		System.out.println("testMessage=" + json);

		producer.send(bytesMessage);
		producer.close();
		
		System.out.println("end:::=");
		return byteArr;
	}

}
