package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.FileInputStream;
import java.util.List;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Test
// @ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class MessageControllerTest extends AbstractTestNGSpringContextTests {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageControllerTest.class);

	private byte[] data;
	private String jsonString;

	@Autowired
	MessageController messageController;

	@BeforeClass
	void beforeclass() throws Exception {
		logger.debug("==========테스트전처리작업시작()==========");
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
		logger.debug("==========테스트전처리작업종료()==========");
	}

	@AfterClass
	void afterclass() throws Exception {
		// pushController.shutdown();
	}

	/**
	 * 메시지 전송 테스트
	 * 
	 * @throws Exception
	 */
	@Test()
	void 메시지전송테스트() throws Exception {
		logger.debug("==========메시지전송테스트()==========");
		Message msg = new Message();
		msg.setSender("@nadir93");
		msg.setReceiver("users/nadir93");
		msg.setMessage(jsonString);
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========메시지전송테스트종료()==========");
	}
}
