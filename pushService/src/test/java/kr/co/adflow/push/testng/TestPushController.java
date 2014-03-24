package kr.co.adflow.push.testng;

import static org.testng.AssertJUnit.assertTrue;

import java.io.FileInputStream;

import kr.co.adflow.push.controller.PushController;
import kr.co.adflow.push.domain.GroupMessage;
import kr.co.adflow.push.domain.IsAvailableResponseData;
import kr.co.adflow.push.domain.PersonalMessage;
import kr.co.adflow.push.domain.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class TestPushController extends AbstractTestNGSpringContextTests {

	private byte[] data;
	private String jsonString;

	@Autowired
	PushController pushController;

	@BeforeClass
	void bfterclass() throws Exception {

		FileInputStream fis = new FileInputStream("mt_location.jpg");
		data = IOUtils.toByteArray(fis);
		String encodedStr = Base64.encodeBase64String(data);
		// System.out.println("encodedBytes::" + encodedStr);
		jsonString = "{\"notification\":{\"contentTitle\":\"교육장소공지\","
				+ "\"contentText\":\"교육장소공지입니다.\", \"ticker\":\"부산은행교육장소알림\n장소: 수림연수원 시간: 3월 22일 오전: 12시\","
				+ "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: 12시\", \"image\":\""
				+ encodedStr
				+ "\"},"
				+ "\"event\":{\"title\":\"부산은행교육\", \"location\":\"수림연수원\", \"desc\":\"\","
				+ "\"year\":\"2014\", \"month\":\"2\"," + "\"day\":\"22\"}"
				+ "}";

		// mqttClient 초기연결시간 기다림
		Thread.sleep(1000);
	}

	@AfterClass
	void afterclass() throws Exception {
		// pushController.shutdown();
	}

	@Test()
	void isAvailable() {
		try {
			Response res = pushController.isAvailable();
			System.out.println("pushServiceAvailable::"
					+ ((IsAvailableResponseData) (res.getResult().getData()))
							.isAvailable());
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test()
	void sendTopic() {
		try {
			GroupMessage grpMsg = new GroupMessage();
			grpMsg.setSender("@nadir93");
			grpMsg.setGroupName("개발1팀");
			grpMsg.setMessage("그룹메시지테스트.");
			Response res = pushController.sendTopic(grpMsg,
					grpMsg.getGroupName());
			System.out.println("sendTopic::isSuccess::"
					+ res.getResult().isSuccess());
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test()
	void sendMessage() {
		try {
			PersonalMessage personalMsg = new PersonalMessage();
			personalMsg.setSender("@nadir93");
			personalMsg.setRecipient("개발사원1");
			personalMsg.setMessage(jsonString);
			Response res = pushController.sendMessage(personalMsg, "userName");
			System.out.println("sendMessage::isSuccess::"
					+ res.getResult().isSuccess());
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
