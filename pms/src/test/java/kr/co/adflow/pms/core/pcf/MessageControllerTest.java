package kr.co.adflow.pms.core.pcf;

import kr.co.adflow.pms.core.controller.MessageController;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.response.MessageSendRes;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class MessageControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MessageController controller;

	@Test(priority = 1)
	void messageSendTest() throws Exception {
		MessageReq messageReq = new MessageReq();

		messageReq.setReceiver("mms/01040269329");
		messageReq.setQos(2);
		messageReq.setExpiry(777730);
		messageReq.setContent("테스트 케이스 메시지 전송 !!");
		messageReq.setContentType("text/plain");

		Response<MessageSendRes> response = controller.sendMessage(
				"c84571f51d56e3e17735eea", messageReq);
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

}
