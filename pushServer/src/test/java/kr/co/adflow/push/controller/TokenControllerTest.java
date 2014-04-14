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

	/**
	 * access token 유효성 체크 테스트
	 */
	@Test()
	void validate() {
		try {
			Response res = tokenController
					.validate("63D31762A50F937B535746C9E31FA33E");
			System.out.println("res=" + res);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
