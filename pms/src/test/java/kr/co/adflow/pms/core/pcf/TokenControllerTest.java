package kr.co.adflow.pms.core.pcf;

import java.io.IOException;
import java.util.HashMap;

import javax.resource.spi.ConnectionManager;

import kr.co.adflow.pms.core.controller.TokenController;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.handler.PCFConnectionManagerHandler;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenInfoRes;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

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

	// @Test(priority = 2)
	// void tokenValidate() throws Exception {
	// TokenReq tokenReq = new TokenReq();
	// tokenReq.setDeviceId("테스트 디바이스 아이디");
	// tokenReq.setDeviceInfo("테스트 디바이스 정보");
	// tokenReq.setUserId("테스트 유저 아이디");
	// tokenReq.setUserName("테스트 유저 네임");
	//
	// try {
	// Response response = controller.validate("c84571f51d56e3e17735eea",
	// "c84571f51d56e3e17735eea");
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

	@Test(priority = 4)
	void getConnections() throws Exception {
		TokenReq tokenReq = new TokenReq();
		tokenReq.setDeviceId("테스트 디바이스 아이디");
		tokenReq.setDeviceInfo("테스트 디바이스 정보");
		tokenReq.setUserId("테스트 유저 아이디");
		tokenReq.setUserName("테스트 유저 네임");
		String token = "0eb3c476f80cfe5dee8cd56";
		logger.debug("token:" + token);

		String statusResult = null;
		ConnectionManager connMan = MQEnvironment.getDefaultConnectionManager();
		MQQueueManager qmgr = null;
		PCFMessageAgent agent = null;
		try {
			qmgr = new MQQueueManager("MQTT", connMan);
			agent = new PCFMessageAgent(qmgr);
			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);
			request.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
			request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE,
					MQConstants.MQCHT_MQTT);
			request.addParameter(MQConstants.MQCACH_CLIENT_ID, token);

			PCFMessage[] response = agent.send(request);

			int chStatus = (int) response[0]
					.getParameterValue(MQConstants.MQIACH_CHANNEL_STATUS);
			if (chStatus == 3) {

				statusResult = "MQTT Connected";
			} else {

				statusResult = "MQTT Disconnected";
			}

		} catch (PCFException pcfe) {
			if (pcfe.getMessage().indexOf("3065") > 0) {
				logger.debug("해당 토큰관련 클라이언트가 Pending 메시지가 없을 경우 채널상태는 없음. -errorcode:3065");
				statusResult = "MQTT Disconnected";
			} else {
				logger.debug("PCF error: " + pcfe);
				statusResult = pcfe.toString();
			}
		} catch (MQException mqe) {
			logger.error("MQException is", mqe);
			throw mqe;
		} catch (IOException ioe) {
			logger.error("IOException is", ioe);
			throw ioe;
		} finally {
			if (agent != null) {
				try {
					agent.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
		}

	}
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
