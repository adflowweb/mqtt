package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import kr.co.adflow.push.dao.impl.DeviceDaoImpl;
import kr.co.adflow.push.dao.impl.TokenDaoImpl;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Token;
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
	
	@Autowired
	DeviceDaoImpl deviceDaoImpl;
	
	@Autowired
	TokenDaoImpl tokenDaoImpl;
	

	String userID = "testUser01";
	String deviceID = "iphoneTest";
	String tokenID = null;

	@BeforeClass
	void beforeclass() throws Exception {
	}

	@AfterClass
	void afterclass() throws Exception {
	}

	/**
	 * 토큰발급 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 1)
	void 토큰발행테스트() throws Exception {
		logger.debug("==========토큰발행테스트시작()==========");
		User user = new User();
		user.setUserID(userID);
		user.setDeviceID(deviceID);
//		user.setPassword("passw0rd");
//		user.setName("testName");
//		user.setDept("webSVC");
//		user.setEmail("typark@adflow.co.kr");
//		user.setPhone("01040269329");
//		user.setTitle("manager");
		Response<Token> res = userController.auth(user, null, null);
		logger.debug("호출결과=" + res);
		
		tokenID = res.getResult().getData().getTokenID();
		logger.debug("Token=" + tokenID);

		if (tokenID != null) {
			assertTrue(true);
		} else {
			assertTrue(false);

		}
		
		logger.debug("==========토큰발행테스트종료()==========");
	}

	/**
	 * 토큰가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 2)
	void 토큰가져오기테스트() throws Exception {
		logger.debug("==========유저가져오기테스트시작()==========");
		Response res = userController.get(userID);
		logger.debug("호출결과=" + res);
		User user = (User) res.getResult().getData();
		assertTrue(user.getUserID().equals(userID));
		logger.debug("==========유저가져오기테스트종료()==========");
		
		logger.debug("==========디바이스가져오기테스트시작()==========");
		Device device = deviceDaoImpl.getByUser(userID);
		logger.debug("호출결과 deviceID =" + device.getDeviceID());
		assertTrue(device.getDeviceID().equals(deviceID));
		logger.debug("==========디바이스가져오기테스트종료()==========");
		
		logger.debug("==========토큰가져오기테스트시작()==========");
		Token token = new Token();
		token.setUserID(userID);
		token = tokenDaoImpl.getLatest(token);
		logger.debug("호출결과 tokenID =" + token.getTokenID());
		assertTrue(token.getTokenID().equals(tokenID));
		logger.debug("==========토큰가져오기테스트종료()==========");
	}

	/**
	 * 토큰 삭제하기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 3)
	void 토큰삭제테스트() throws Exception {
		logger.debug("==========토큰삭제테스트시작()==========");
		int result = tokenDaoImpl.delete(tokenID);
		logger.debug("호출결과=" + result);
		assertEquals(result, 1);	
		logger.debug("==========토큰삭제테스트종료()==========");
		
		logger.debug("==========디바이스삭제테스트시작()==========");
		result = deviceDaoImpl.delete(deviceID);
		logger.debug("호출결과=" + result);
		assertEquals(result, 1);	
		logger.debug("==========디바이스삭제테스트종료()==========");
		
		
		logger.debug("==========유저삭제테스트시작()==========");
		Response res = userController.delete(userID);
		logger.debug("호출결과 user =" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");
		logger.debug("==========유저삭제테스트종료()==========");
		
		
		
		
	}
}
