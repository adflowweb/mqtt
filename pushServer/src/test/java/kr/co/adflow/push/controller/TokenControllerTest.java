package kr.co.adflow.push.controller;

import static org.testng.AssertJUnit.assertTrue;
import kr.co.adflow.push.domain.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Test
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class TokenControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	TokenController tokenController;

	@Test()
	void validate() {
		try {
			Response res = tokenController.validate("accessToken");
			System.out.println("res=" + res);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
