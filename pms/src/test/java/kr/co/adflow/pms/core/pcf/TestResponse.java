package kr.co.adflow.pms.core.pcf;

import java.util.HashMap;

import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.response.Response;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class TestResponse<T> extends AbstractTestNGSpringContextTests {

	@Test
	void resoponseTest() {

		// AuthReq authReq = new AuthReq();
		// Response<T> test = new Response<T>();
		//
		// test.setCode("500");
		// HashMap<String, String> hashMapData = new HashMap<>();
		// hashMapData.put("userId", "찬호");
		//
		// test.setData(authReq);
		// test.setExplaination("설명");
		// test.setMessage("메시지");
		// test.setStatus("ok");

	}

}
