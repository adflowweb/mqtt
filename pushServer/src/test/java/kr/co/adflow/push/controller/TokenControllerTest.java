package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.Validation;

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
public class TokenControllerTest extends AbstractTestNGSpringContextTests {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenControllerTest.class);

	@Autowired
	TokenController tokenController;

	@Autowired
	UserController userController;

	@Autowired
	DeviceController deviceController;

	String tokenID;
	String userID = "tokenTestUser";
	String deviceID = "tokenTestDevice";

	/**
	 * @throws Exception
	 */
	@BeforeClass
	void beforeclass() throws Exception {
		logger.debug("==========테스트전처리작업시작()==========");
		Response res = userController.delete(userID);
		logger.debug("호출결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);

		res = deviceController.delete(deviceID);
		logger.debug("호출결과=" + res);
		infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		logger.debug("==========테스트전처리작업종료()==========");
	}

	/**
	 * @throws Exception
	 */
	@AfterClass
	void afterclass() throws Exception {
		logger.debug("==========테스트후처리작업시작()==========");
		Response res = userController.delete(userID);
		logger.debug("호출결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);

		res = deviceController.delete(deviceID);
		logger.debug("호출결과=" + res);
		infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		logger.debug("==========테스트후처리작업종료()==========");
	}

	/**
	 * 토큰발행 입력오류 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 1)
	void 토큰발행오류테스트() throws Exception {
		logger.debug("==========토큰발행오류테스트시작()==========");
		Token token = new Token();
		// token.setTokenID("63D31762A50F937B535746C9E31FA33E");
		// token.setDeviceID("nexus5");
		token.setUserID(userID);
		// token.setIssue(new Date());
		Response res = tokenController.post(token);
		logger.debug("호출결과=" + res);
		List<String> errors = res.getResult().getErrors();
		logger.debug("errors=" + errors);
		assertNotNull(errors);
		logger.debug("==========토큰발행오류테스트종료()==========");
	}

	/**
	 * 토큰발행 테스트
	 */
	@Test(priority = 1)
	void 토큰발행테스트() throws Exception {
		logger.debug("==========토큰발행테스트시작()==========");
		Token token = new Token();
		// token.setTokenID("63D31762A50F937B535746C9E31FA33E");
		token.setDeviceID(deviceID);
		token.setUserID(userID);
		// token.setIssue(new Date());
		Response res = tokenController.post(token);
		logger.debug("호출결과=" + res);
		Token rst = (Token) res.getResult().getData();
		logger.debug("token=" + rst);
		tokenID = rst.getTokenID();
		assertNotNull(tokenID);
		logger.debug("==========토큰발행테스트종료()==========");

	}

	/**
	 * access token 유효성 체크 테스트
	 */
	@Test(priority = 2)
	void 토큰유효성검증테스트() throws Exception {
		logger.debug("==========토큰유효성검증테스트시작()==========");
		Response res = tokenController.validate(tokenID);
		logger.debug("호출결과=" + res);
		Validation validation = (Validation) res.getResult().getData();
		assertTrue(validation.isValidation());
		logger.debug("==========토큰유효성검증테스트종료()==========");
	}

	/**
	 * 토큰정보 삭제하기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 3)
	void 토큰정보삭제테스트() throws Exception {
		logger.debug("==========토큰정보삭제테스트시작()==========");
		Response res = tokenController.delete(tokenID);
		logger.debug("호출결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");
		logger.debug("==========토큰정보삭제테스트종료()==========");
	}
}
