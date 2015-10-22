package kr.co.adflow.pms.core.pcf;

import java.util.HashMap;

import kr.co.adflow.pms.core.controller.TokenController;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenInfoRes;
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

	// @Test(priority = 1)
	// void tokenCreate() throws Exception {
	//
	// TokenReq tokenReq = new TokenReq();
	// tokenReq.setDeviceId("테스트 디바이스 아이디1");
	// tokenReq.setDeviceInfo("테스트 디바이스 정보");
	// tokenReq.setUserId("테스트 유저 아이디1");
	// tokenReq.setUserName("테스트 유저 네임");
	//
	// try {
	// Response response = controller.createToken(
	// "c84571f51d56e3e17735eea", tokenReq);
	//
	// String result = response.getStatus();
	// logger.debug(result);
	// System.out.println(result);
	// Assert.assertEquals(result, "ok");
	//
	// } catch (TokenRunTimeException e) {
	// // TODO Auto-generated catch block
	// HashMap<String, String> map = e.getErrorMsg();
	// System.out.println(map.get("errorCode"));
	// System.out.println(map.get("errorMsg"));
	// e.printStackTrace();
	// }
	//
	// }

//	@Test(priority = 2)
//	void tokenValidate() throws Exception {
//		TokenReq tokenReq = new TokenReq();
//		tokenReq.setDeviceId("테스트 디바이스 아이디");
//		tokenReq.setDeviceInfo("테스트 디바이스 정보");
//		tokenReq.setUserId("테스트 유저 아이디");
//		tokenReq.setUserName("테스트 유저 네임");
//
//		try {
//			Response response = controller.validate("c84571f51d56e3e17735eea",
//					"c84571f51d56e3e17735eea");
//
//			String result = response.getStatus();
//			logger.debug(result);
//			System.out.println(result);
//			Assert.assertEquals(result, "ok");
//
//		} catch (TokenRunTimeException e) {
//			// TODO Auto-generated catch block
//			HashMap<String, String> map = e.getErrorMsg();
//			System.out.println(map.get("errorCode"));
//			System.out.println(map.get("errorMsg"));
//			e.printStackTrace();
//		}
//
//	}
	//
	 @Test(priority = 3)
	 void getToken() throws Exception {
	 TokenReq tokenReq = new TokenReq();
	 tokenReq.setDeviceId("테스트 디바이스 아이디");
	 tokenReq.setDeviceInfo("테스트 디바이스 정보");
	 tokenReq.setUserId("테스트 유저 아이디");
	 tokenReq.setUserName("테스트 유저 네임");
	
	 try {
	 Response<TokenInfoRes> response = controller
	 .getToken("c84571f51d56e3e17735eea");
	
	 String result = response.getStatus();
	 logger.debug(result);
	 System.out.println(result);
	 System.out.println(response.toString());
	 Assert.assertEquals(result, "ok");
	
	 } catch (TokenRunTimeException e) {
	 // TODO Auto-generated catch block
	 HashMap<String, String> map = e.getErrorMsg();
	 System.out.println(map.get("errorCode"));
	 System.out.println(map.get("errorMsg"));
			e.printStackTrace();
		}

	}

	// @Test(priority = 4)
	// void getConnections() throws Exception {
	// TokenReq tokenReq = new TokenReq();
	// tokenReq.setDeviceId("테스트 디바이스 아이디");
	// tokenReq.setDeviceInfo("테스트 디바이스 정보");
	// tokenReq.setUserId("테스트 유저 아이디");
	// tokenReq.setUserName("테스트 유저 네임");
	//
	// try {
	// Response response = controller
	// .getTokenStatus("c84571f51d56e3e17735eea");
	//
	// String result = response.getStatus();
	// logger.debug(result);
	// System.out.println(result);
	// System.out.println(response.toString());
	// Assert.assertEquals(result, "ok");
	//
	// } catch (TokenRunTimeException e) {
	// // TODO Auto-generated catch block
	// HashMap<String, String> map = e.getErrorMsg();
	// System.out.println(map.get("errorCode"));
	// System.out.println(map.get("errorMsg"));
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Test(priority = 5)
	// void getSubscriptions() throws Exception {
	// TokenReq tokenReq = new TokenReq();
	// tokenReq.setDeviceId("테스트 디바이스 아이디");
	// tokenReq.setDeviceInfo("테스트 디바이스 정보");
	// tokenReq.setUserId("테스트 유저 아이디");
	// tokenReq.setUserName("테스트 유저 네임");
	//
	// try {
	// Response response = controller.getTopic("c84571f51d56e3e17735eea");
	//
	// String result = response.getStatus();
	// logger.debug(result);
	// System.out.println(result);
	// System.out.println(response.toString());
	// Assert.assertEquals(result, "ok");
	//
	// } catch (TokenRunTimeException e) {
	// // TODO Auto-generated catch block
	// HashMap<String, String> map = e.getErrorMsg();
	// System.out.println(map.get("errorCode"));
	// System.out.println(map.get("errorMsg"));
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Test(priority = 6)
	// void getTopicInfo() throws Exception {
	// try {
	// Response response = controller.getToken("c84571f51d56e3e17735eea");
	//
	// String result = response.getStatus();
	// logger.debug(result);
	// System.out.println(result);
	// System.out.println(response.toString());
	// Assert.assertEquals(result, "ok");
	//
	// } catch (TokenRunTimeException e) {
	// // TODO Auto-generated catch block
	// HashMap<String, String> map = e.getErrorMsg();
	// System.out.println(map.get("errorCode"));
	// System.out.println(map.get("errorMsg"));
	// e.printStackTrace();
	// }
	//
	// }

}
