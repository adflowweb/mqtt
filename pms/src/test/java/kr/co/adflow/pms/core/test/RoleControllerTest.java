package kr.co.adflow.pms.core.test;

import kr.co.adflow.pms.core.controller.GroupController;
import kr.co.adflow.pms.core.controller.RoleController;
import kr.co.adflow.pms.core.request.GroupReq;
import kr.co.adflow.pms.core.request.RoleReq;
import kr.co.adflow.pms.core.response.GroupListRes;
import kr.co.adflow.pms.core.response.GroupRes;
import kr.co.adflow.pms.core.response.RoleListRes;
import kr.co.adflow.pms.core.response.RoleRes;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class RoleControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	RoleController controller;

	@Test(priority = 1)
	void createRole() throws Exception {

		RoleReq roleReq = new RoleReq();
		roleReq.setApiCode(111);
		roleReq.setApiMethod("POST");
		roleReq.setApiName("테스트메소드");
		roleReq.setApiUrl("/test");
		roleReq.setGroupCode(50000);

		Response<RoleRes> response = controller.createRole(
				"0eb3c476f80cfe5dee8cd56", roleReq);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 2)
	void getRole() throws Exception {

		Response<RoleListRes> response = controller
				.getRole("0eb3c476f80cfe5dee8cd56");

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 3)
	void updateRole() throws Exception {
		RoleReq roleReq = new RoleReq();
		roleReq.setApiCode(222);

		Response response = controller.updateRole("0eb3c476f80cfe5dee8cd56",
				50000, 111, roleReq);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 4)
	void deleteRole() throws Exception {

		Response response = controller.deleteRole("0eb3c476f80cfe5dee8cd56",
				50000, 222);
		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

}
