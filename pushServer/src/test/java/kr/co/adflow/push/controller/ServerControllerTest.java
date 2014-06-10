package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertNotNull;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.ServerInfo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
// @ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class ServerControllerTest extends AbstractTestNGSpringContextTests {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(ServerControllerTest.class);

	@Autowired
	ServerController serverController;

	@BeforeClass
	void beforeclass() throws Exception {
		// mqttClient 초기연결시간 기다림
		Thread.sleep(1000);
	}

	/**
	 * 서버정보 가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test()
	void 서버정보가져오기테스트() throws Exception {
		logger.debug("==========서버정보가져오기테스트시작()==========");
		Response res = serverController.get();
		logger.debug("호출결과=" + res);
		ServerInfo data = (ServerInfo) res.getResult().getData();
		assertNotNull(data.getCpu());
		// data.getCpu().
		// assertTrue(data.isAvailable());
		logger.debug("==========서버정보가져오기테스트종료()==========");
	}
}
