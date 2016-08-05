package kr.co.adflow.push.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.domain.ktp.request.Precheck;
import kr.co.adflow.push.domain.ktp.request.SessionClean;
import kr.co.adflow.push.ktp.controller.PlatformController;

@Test
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class PlatfromControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	PlatformController controller;

	@Test
	void sendCleanSession() {
		SessionClean sessionClean = new SessionClean();

		sessionClean.setReceiver("/sessionClean");
		sessionClean.setSender("testngsend");
		sessionClean.setContent("sessionCleanContent");

		// Assert.assertEquals("test", "test");

		try {
			controller.sessionClean(sessionClean);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void digAccount() {

		// Assert.assertEquals("test", "test");
		DigInfo digInfo = new DigInfo();
		digInfo.setContent("{\"123\":\"123\"}");
		digInfo.setContentType("application/json");
		digInfo.setReceiver("erer");
		digInfo.setSender("aa");
		try {
			controller.modifyDigInfo(digInfo);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void keepAliveTime() {

		KeepAliveTime keepAliveTime = new KeepAliveTime();
		keepAliveTime.setContent("{\"123\":\"123\"}");
		keepAliveTime.setSender("sender");
		keepAliveTime.setReceiver("reciever");

		try {
			controller.modifyKeepAliveTime(keepAliveTime);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void fwInfo() {

		FwInfo fwInfo = new FwInfo();
		fwInfo.setContent("{\"123\":\"123\"}");
		fwInfo.setContentType("application/json");
		fwInfo.setReceiver("receiver");
		fwInfo.setSender("sender");

		try {
			controller.modifyFwInfo(fwInfo);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void precheck() throws Exception {
		Precheck precheck = new Precheck();
		precheck.setReceiver("reciever");
		precheck.setSender("sender");
		controller.sendPrecheck(precheck);
	}
}
