package kr.co.adflow.jersey;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import kr.co.adflow.push.auth.wmq.JAASLoginModule;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public class RestClientTest {
	private static final Logger logger = LoggerFactory
			.getLogger(RestClientTest.class);

	private static final String API_KEY = "devServer2";

	private WebResource webResource;
	private RestClient client;
	private String userID = "RestClientTest";
	private String deviceID = "RestClientTest";
	private String tokenID;

	@BeforeClass
	void beforeclass() throws Exception {
		logger.debug("==========테스트전처리작업시작()==========");
		client = new RestClient();
		webResource = client.getWebResource();
		// webResource.setProperty("X-ApiKey", API_KEY);

		Token token = new Token();
		token.setDeviceID(deviceID);
		token.setUserID(userID);
		token.setIssue(new Date());
		Response<Token> data = webResource.path("tokens")
				.header("X-ApiKey", API_KEY).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(new GenericType<Response<Token>>() {
				}, token);

		logger.debug("호출결과=" + data);
		Token rst = data.getResult().getData();
		tokenID = rst.getTokenID();
		assertNotNull(tokenID);
		logger.debug("==========테스트전처리작업종료()==========");
	}

	@AfterClass
	void afterclass() throws Exception {
		logger.debug("==========테스트후처리작업시작()==========");

		// delete token
		Response<?> res = webResource.path("tokens").path(tokenID)
				.header("X-ApiKey", API_KEY).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).delete(Response.class);
		logger.debug("토큰삭제결과=" + res);
		List<String> infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");

		// delete device
		res = webResource.path("devices").path(deviceID)
				.header("X-ApiKey", API_KEY).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).delete(Response.class);
		logger.debug("디바이스삭제결과=" + res);
		infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");

		// delete user
		res = webResource.path("users").path(userID)
				.header("X-ApiKey", API_KEY).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).delete(Response.class);
		logger.debug("유저삭제결과=" + res);
		infos = res.getResult().getInfo();
		logger.debug("infos=" + infos);
		assertEquals(infos.get(0), "updates=1");

		logger.debug("==========테스트후처리작업시작()==========");
	}

	/**
	 * 프로퍼티 파일 로딩테스트
	 * 
	 * @throws Exception
	 */
	@Test
	public void 프로퍼티파일로딩테스트() throws Exception {
		logger.debug("==========프로퍼티파일로딩테스트시작()==========");
		Properties prop = new Properties();
		prop.load(JAASLoginModule.class
				.getResourceAsStream("/config.properties"));
		logger.debug("설정값=" + prop);
		assertEquals(prop.getProperty("server.ip"), "127.0.0.1");
		logger.debug("==========프로퍼티파일로딩테스트종료()==========");
	}

	/**
	 * access token 유효성 체크
	 * 
	 * @throws Exception
	 */
	@Test
	public void 토큰유효성체크테스트() throws Exception {
		logger.debug("==========토큰유효성체크테스트시작()==========");
		Validation response = client.validate(tokenID);
		// Validation data = (Validation) response.getResult().getData();
		logger.debug("호출결과=" + response);
		assertTrue(response != null && response.isValidation());
		logger.debug("==========토큰유효성체크테스트종료()==========");
	}
}
