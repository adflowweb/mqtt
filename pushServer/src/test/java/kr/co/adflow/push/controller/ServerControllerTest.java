package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertTrue;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.ServerInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class ServerControllerTest extends AbstractTestNGSpringContextTests {
	@Autowired
	ServerController serverController;

	@BeforeClass
	void bfterclass() throws Exception {
		// mqttClient 초기연결시간 기다림
		Thread.sleep(1000);
	}

	/**
	 * 서버정보 가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test()
	void get() throws Exception {

		Response res = serverController.get();
		System.out.println("res=" + res);
		ServerInfo data = (ServerInfo) res.getResult()
				.getData();
		assertTrue(data.isAvailable());

	}
}
