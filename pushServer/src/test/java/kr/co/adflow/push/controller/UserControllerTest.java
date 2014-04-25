package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertTrue;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.User;

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
public class UserControllerTest extends AbstractTestNGSpringContextTests {
	@Autowired
	UserController userController;

	@BeforeClass
	void bfterclass() throws Exception {
	}

	@AfterClass
	void afterclass() throws Exception {
	}

	/**
	 * 유저정보입력 테스트
	 */
	@Test(priority = 1)
	void post() {
		try {
			User user = new User();
			user.setUserID("testUser");
			user.setName("testName");
			user.setDept("webSVC");
			user.setEmail("typark@adflow.co.kr");
			user.setPhone("01040269329");
			user.setTitle("manager");
			Response res = userController.post(user);
			System.out.println("res=" + res);
			assertTrue(true);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	/**
	 * 유저정보 가져오기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 2)
	void get() throws Exception {
		Response res = userController.get("testUser");
		System.out.println("res=" + res);
		assertTrue(true);
	}

	/**
	 * 유저정보 삭제하기 테스트
	 * 
	 * @throws Exception
	 */
	@Test(priority = 3)
	void delete() throws Exception {
		Response res = userController.delete("testUser");
		System.out.println("res=" + res);
		assertTrue(true);
	}
}
