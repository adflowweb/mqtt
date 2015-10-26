package kr.co.adflow.pms.core.pcf;

import kr.co.adflow.pms.core.controller.UserController;
import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.response.UserInfoRes;
import kr.co.adflow.pms.core.response.UserRes;
import kr.co.adflow.pms.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class UserControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	UserController controller;

	@Test(priority = 1)
	void createUser() throws Exception {
		UserReq userReq = new UserReq();
		userReq.setUserId("IODOCS");
		userReq.setUserName("아이오닥스");
		userReq.setPassword("password");
		userReq.setRole(1);

		Response<UserRes> response = controller.createUser(
				"c84571f51d56e3e17735eea", userReq);

		Assert.assertEquals(response.getStatus(), "ok");
		System.out.println(response.toString());
	}

	// @Test(priority = 2)
	// void getUserInfo() throws Exception {
	//
	// String userId = "케이스관리자4";
	//
	// Response<UserInfoRes> response = controller.getUsers(userId,
	// "c84571f51d56e3e17735eea");
	//
	// Assert.assertEquals(response.getStatus(), "ok");
	// System.out.println(response.toString());
	// }
	//
	// @Test(priority = 3)
	// void updateUser() throws Exception {
	//
	// String userId = "케이스관리자4";
	// UserReq userReq = new UserReq();
	// userReq.setUserName("이름을 홍길동으로");
	//
	// Response response = controller.updateUser(userId, userReq,
	// "c84571f51d56e3e17735eea");
	//
	// Assert.assertEquals(response.getStatus(), "ok");
	// System.out.println(response.toString());
	// }
	//
	// @Test(priority = 4)
	// void deleteUser() throws Exception {
	//
	// String userId = "케이스관리자4";
	//
	// Response response = controller.deleteUser(userId,
	// "c84571f51d56e3e17735eea");
	//
	// Assert.assertEquals(response.getStatus(), "ok");
	// System.out.println(response.toString());
	// }

}
