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
	@Test(priority = 1)
	void 메시지전송테스트() throws Exception {
		logger.debug("==========메시지전송테스트시작()==========");
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/nadir93");
		jsonString = "메시지전송테스트";
		msg.setContent(jsonString);
		msg.setQos(1);
		msg.setSms(false);
		// msg.setReservation(new Date());
		// msg.setIssue(new Date());
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========메시지전송테스트종료()==========");
	}

	/**
	 * 예약메시지 전송 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 2)
	void 예약메시지전송테스트() throws Exception {
		logger.debug("==========예약메시지전송테스트시작()==========");

		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/nadir93");
		jsonString = "예약메시지전송테스트";
		msg.setContent(jsonString);
		msg.setQos(1);
		// Date sendDate = new Date();
		// sendDate.setMinutes(sendDate.getMinutes() + 10);

		cal.set(cal.SECOND, 0);
		cal.add(cal.MINUTE, +2); // 2분뒤
		Date sendDate = cal.getTime();
		sendDate.setSeconds(0);
		msg.setReservation(sendDate); // 분단위 지정
		// msg.setSms(false);
		// msg.setTimeOut(5); // 분단위
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========예약메시지전송테스트종료()==========");
	}

	/**
	 * SMS 전송 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 3)
	void SMS메시지전송테스트() throws Exception {
		logger.debug("==========SMS메시지전송테스트시작()==========");
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/nadir93");
		jsonString = "SMS메시지전송테스트";
		msg.setContent(jsonString);
		msg.setQos(1);
		// msg.setReservation(new Date());
		msg.setSms(true);
		msg.setTimeOut(5); // 분단위
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========SMS메시지전송테스트종료()==========");
	}

	/**
	 * 예약SMS 전송 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 4)
	void 예약SMS메시지전송테스트() throws Exception {
		logger.debug("==========예약SMS메시지전송테스트시작()==========");
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/nadir93");
		jsonString = "예약SMS메시지전송테스트";
		msg.setContent(jsonString);
		msg.setQos(1);

		Date sendDate = cal.getTime();
		sendDate.setSeconds(0);
		msg.setReservation(sendDate);
		msg.setSms(true);
		msg.setTimeOut(5); // 분단위
		Response res = messageController.post(msg);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========예약SMS메시지전송테스트종료()==========");
	}

	/**
	 * 메시지 가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 5)
	void 메시지가져오기테스트() throws Exception {
		logger.debug("==========메시지가져오기테스트시작()==========");
		Response res = messageController.get(19);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNull(errors);
		logger.debug("==========메시지가져오기테스트종료()==========");
	}
}
