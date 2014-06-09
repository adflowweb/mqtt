package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.User;

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
public class UserControllerTest extends AbstractTestNGSpringContextTests {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserControllerTest.class);

	@Autowired
	UserController userController;

	String userID = "testUser01";

	@BeforeClass
	void beforeclass() throws Exception {
	}

	@AfterClass
	void afterclass() throws Exception {
	}

	/**
	 * 유저정보입력 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 1)
	void 유저정보입력테스트() throws Exception {
		logger.debug("==========유저정보입력테스트시작()==========");
		User user = new User();
		user.setUserID(userID);
		user.setPassword("passw0rd");
		user.setName("testName");
		user.setDept("webSVC");
		user.setEmail("typark@adflow.co.kr");
		user.setPhone("01040269329");
		user.setTitle("manager");
		Response res = userController.post(user);
		logger.debug("호출결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");
		logger.debug("==========유저정보입력테스트종료()==========");
	}

	/**
	 * 유저정보 가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 2)
	void 유저정보가져오기테스트() throws Exception {
		logger.debug("==========유저정보가져오기테스트시작()==========");
		Response res = userController.get(userID);
		logger.debug("호출결과=" + res);
		User user = (User) res.getResult().getData();
		assertTrue(user.getUserID().equals(userID));
		logger.debug("==========유저정보가져오기테스트종료()==========");
	}

	/**
	 * 유저정보 삭제하기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 3)
	void 유저정보삭제테스트() throws Exception {
		logger.debug("==========유저정보삭제테스트시작()==========");
		Response res = userController.delete(userID);
		logger.debug("호출결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");
		logger.debug("==========유저정보삭제테스트종료()==========");
	}
}
