package kr.co.adflow.pms.core.test;

import kr.co.adflow.pms.core.controller.MonitorController;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class MonitorControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MonitorController controller;

	@Test(priority = 1)
	void getServerInfo() throws Exception {

		Response<ServerInfo> response = controller
				.getServerInfo("0eb3c476f80cfe5dee8cd56");

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

}
