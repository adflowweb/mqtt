package kr.co.adflow.pms.core.pcf;

import java.util.HashMap;
import java.util.Map;

import kr.co.adflow.pms.core.controller.MessageController;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.response.AckRes;
import kr.co.adflow.pms.core.response.MessageSendRes;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.core.response.SubscriptionsRes;
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

		messageReq.setReceiver("testTopic");
		messageReq.setQos(2);
		messageReq.setExpiry(777730);
		messageReq.setContent("테스트 케이스 메시지 전송 !!");
		messageReq.setContentType("text/plain");

		Response<MessageSendRes> response = controller.sendMessage(
				"c84571f51d56e3e17735eea", messageReq);
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 2)
	void getMessage() throws Exception {

		HashMap<String, String> queryParam = new HashMap<String, String>();
		queryParam.put("iDisplayStart", "0");
		queryParam.put("iDisplayLength", "20");
		queryParam.put("cSearchStatus", "ALL");
		queryParam.put("cSearchDateStart", "2015-09-01T15:00:00.000Z");
		queryParam.put("cSearchDateEnd", "2015-10-31T15:00:00.000Z");
		queryParam.put("cSearchFilterContent", "100");
		queryParam.put("cSearchFilter", "msgType");

		Response<MessagesListRes> response = controller.getMessageList(
				queryParam, "c84571f51d56e3e17735eea");
		System.out.println(response.toString());
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 3)
	void getStatistics() throws Exception {

		HashMap<String, String> queryParam = new HashMap<String, String>();

		queryParam.put("cSearchDateStart", "2015-09-01T15:00:00.000Z");
		queryParam.put("cSearchDateEnd", "2015-10-31T15:00:00.000Z");

		Response<StatisticsRes> response = controller.getMessageStatistics(
				queryParam, "c84571f51d56e3e17735eea");
		System.out.println(response.toString());
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 4)
	void getAckMessage() throws Exception {

		HashMap<String, String> queryParam = new HashMap<String, String>();

		queryParam.put("cSearchDateStart", "2015-09-01T15:00:00.000Z");
		queryParam.put("cSearchDateEnd", "2015-10-31T15:00:00.000Z");

		Response<AckRes> response = controller.getAckMessage(queryParam,
				"2015106b5686a955ac4f55a37c726c9abf9b90",
				"c84571f51d56e3e17735eea");
		System.out.println(response.toString());
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 5)
	void messageSystemTest() throws Exception {
		MessageReq messageReq = new MessageReq();

		messageReq.setReceiver("testTopic");
		messageReq.setQos(2);
		messageReq.setExpiry(777730);
		messageReq.setContent("200");
		messageReq.setContentType("text/plain");

		Response<MessageSendRes> response = controller.sendSystemMessage(
				"c84571f51d56e3e17735eea", messageReq, 200);
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}
}
