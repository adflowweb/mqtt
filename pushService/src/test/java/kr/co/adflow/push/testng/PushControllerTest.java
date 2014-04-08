package kr.co.adflow.push.testng;

import static org.testng.AssertJUnit.assertTrue;

import java.io.FileInputStream;

import kr.co.adflow.push.controller.PushController;
import kr.co.adflow.push.domain.Message;
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
public class PushControllerTest extends AbstractTestNGSpringContextTests {

	private byte[] data;
	private String jsonString;

	@Autowired
	PushController pushController;

	@BeforeClass
	void bfterclass() throws Exception {

		FileInputStream fis = new FileInputStream(
				"src/test/resources/mt_location.jpg");
		data = IOUtils.toByteArray(fis);
		String encodedStr = Base64.encodeBase64String(data);
		// System.out.println("encodedBytes::" + encodedStr);
		jsonString = "{\"notification\":{\"contentTitle\":\"교육장소공지\","
				+ "\"contentText\":\"교육장소공지입니다.\", \"ticker\":\"부산은행교육장소알림\n장소: 수림연수원 시간: 3월 22일 오전: 12시\","
				+ "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: 12시\", \"image\":\""
				+ "encodedStr"
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
			System.out.println("pushServiceAvailable=" + res);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test()
	void sendGroupMessage() {
		try {
			Message msg = new Message();
			msg.setSender("@nadir93");
			msg.setMessage("그룹메시지테스트.");
			Response res = pushController.sendGroupMessage(msg, "개발1팀");
			System.out.println("sendGroupMessage=" + res);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test()
	void sendPersonalMessage() {
		try {
			Message msg = new Message();
			msg.setSender("@nadir93");
			msg.setMessage(jsonString);
			Response res = pushController.sendPersonalMessage(msg, "개발사원1");
			System.out.println("sendPersonalMessage=" + res);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
