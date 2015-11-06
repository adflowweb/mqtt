package kr.co.adflow.pms.core.test;

import kr.co.adflow.pms.core.controller.GroupController;
import kr.co.adflow.pms.core.request.GroupReq;
import kr.co.adflow.pms.core.response.GroupListRes;
import kr.co.adflow.pms.core.response.GroupRes;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class GroupControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	GroupController controller;

	@Test(priority = 1)
	void createGroup() throws Exception {
		GroupReq groupReq = new GroupReq();
		groupReq.setGroupCode(70000);
		groupReq.setGroupName("그룹생섣테스트");
		groupReq.setGroupDescription("테스트코드 작성 그룹생성");

		Response<GroupRes> response = controller.createGroup(
				"0eb3c476f80cfe5dee8cd56", groupReq);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 2)
	void getGroup() throws Exception {

		Response<GroupListRes> response = controller
				.getGroups("0eb3c476f80cfe5dee8cd56");

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 3)
	void updateGroup() throws Exception {
		GroupReq groupReq = new GroupReq();
		groupReq.setGroupCode(80000);

		Response response = controller.updateGroup("0eb3c476f80cfe5dee8cd56",
				70000, groupReq);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	@Test(priority = 4)
	void deleteGroup() throws Exception {

		Response response = controller.deleteGroup("0eb3c476f80cfe5dee8cd56",
				80000);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

}
