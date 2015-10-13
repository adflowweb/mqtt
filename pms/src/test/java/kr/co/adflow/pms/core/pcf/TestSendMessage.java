package kr.co.adflow.pms.core.pcf;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class TestSendMessage extends AbstractTestNGSpringContextTests {
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	PmsConfig pmsConfig;

	@Test()
	void testMessageService() {
		Message msg = new Message();
		msg.setSender("/test/chan");
		msg.setReceiverTopic("mms/01040269329");
		msg.setQos(2);
		msg.setContent("테스트 케이스 메시지 전송 !!");
		msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_ADFLOW);
		msg.setAck(true);
		msg.setContentType("text/plain");
		msg.setIssueId("생성자 아이디 ");
		msg.setMsgType(106);
		msg.setIssueName("메시지보낸사람이름");
		KeyGenerator keyGenerator = new KeyGenerator();
		msg.setMsgId(keyGenerator.generateMsgId());
		jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate,
				msg));

	}

}
