package kr.co.adflow.pms.core.pcf;

import java.util.HashMap;

import kr.co.adflow.pms.core.controller.TokenController;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class TokenControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	TokenController controller;

	@Test
	void test() {

		TokenReq tokenReq = new TokenReq();
		tokenReq.setDeviceId("테스트 디바이스 아이디");
		tokenReq.setDeviceInfo("테스트 디바이스 정보");
		tokenReq.setUserId("테스트 유저 아이디");
		tokenReq.setUserName("테스트 유저 네임");

		try {
			Response response = controller.createToken(
					"c84571f51d56e3e17735eea", tokenReq);

			String result = response.getStatus();
			logger.debug(result);
			System.out.println(result);
			Assert.assertEquals(response.getStatus(), result);

		} catch (TokenRunTimeException e) {
			// TODO Auto-generated catch block
			HashMap<String, String> map = e.getErrorMsg();
			System.out.println(map.get("errorCode"));
			System.out.println(map.get("errorMsg"));
			e.printStackTrace();
		}

	}
}
