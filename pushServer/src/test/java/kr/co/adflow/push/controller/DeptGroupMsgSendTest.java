package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertNull;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
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

@Test
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class DeptGroupMsgSendTest extends AbstractTestNGSpringContextTests {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DeptGroupMsgSendTest.class);

	private byte[] data;
	private String jsonString;
	private Calendar cal = Calendar.getInstance();

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

		// jsonString = "안녕하세요3";

		// jsonString = "{\"notification\":{\"contentTitle\":\"교육장소공지\","
		// +
		// "\"contentText\":\"교육장소공지입니다.\", \"ticker\":\"부산은행교육장소알림\n장소: 수림연수원 시간: 3월 22일 오전: 12시\","
		// + "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: "
		// + (int) (Math.random() * 100)
		// + "시\", \"image\":\""
		// + "encodedStr"
		// + "\"},"
		// +
		// "\"event\":{\"title\":\"부산은행교육\", \"location\":\"수림연수원\", \"desc\":\"\","
		// + "\"year\":\"2014\", \"month\":\"2\"," + "\"day\":\"22\"}"
		// + "}";

		// {"notification":{"notificationStyle":1,"contentTitle":"교육장소공지","contentText":"교육장소공지입니다.",
		// "ticker":"부산은행교육장소알림\n장소: 수림연수원 시간: 3월 22일 오전: 12시","summaryText":"장소: 수림연수원 시간: 3월 22일 오전:1시",
		// "image":""},"event":{"title":"부산은행교육", "location":"수림연수원",
		// "desc":"","year":"2014", "month":"2","day":"2"}}

		// mqttClient 초기연결시간 기다림
		Thread.sleep(1000);
		logger.debug("==========테스트전처리작업종료()==========");
	}

	@AfterClass
	void afterclass() throws Exception {
		// pushController.shutdown();
	}

	/**
	 * 부서그룹메시지전송테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 1)
	void 부서그룹메시지전송테스트() throws Exception {
		logger.debug("==========부서그룹메시지전송테스트시작()==========");
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/groups/000120_BSCP");
		jsonString = "{\"notification\":{\"notificationStyle\":1,\"contentTitle\":\"교육장소공지\","
				+ "\"contentText\":\"메시지전송테스트.\", \"ticker\":\"부산은행교육장소알림장소: 수림연수원 시간: 3월 22일 오전: 12시\","
				+ "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: "
				+ (int) (Math.random() * 100)
				+ "시\", \"image\":\""
				+ "encodedStr"
				+ "\"},"
				+ "\"event\":{\"title\":\"부산은행교육\", \"location\":\"수림연수원\", \"desc\":\"\","
				+ "\"year\":\"2014\", \"month\":\"2\","
				+ "\"day\":\"22\"}"
				+ "}";
		msg.setContent(jsonString);
		msg.setQos(1);
		msg.setSms(true);
		msg.setTimeOut(1);
		msg.setType(Message.NOTIFICATION_GROUP_DEPT); // 부서그룹메시지타입 = 3
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========부서그룹메시지전송테스트종료()==========");
	}
}
