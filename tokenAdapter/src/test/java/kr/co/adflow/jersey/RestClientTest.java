package kr.co.adflow.jersey;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Properties;

import kr.co.adflow.push.auth.wmq.JAASLoginModule;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public class RestClientTest {
	private static final Logger logger = LoggerFactory
			.getLogger(RestClientTest.class);

	RestClient client;

	@BeforeClass
	void bfterclass() throws Exception {
		client = new RestClient();
	}

	/**
	 * 프로퍼티 파일 로딩테스트
	 * 
	 * @throws Exception
	 */
	@Test
	public void loadPropertiesFile() throws Exception {
		Properties prop = new Properties();
		prop.load(JAASLoginModule.class
				.getResourceAsStream("/config.properties"));
		logger.debug("properties=" + prop);
	}

	/**
	 * access token 유효성 체크
	 * 
	 * @throws Exception
	 */
	@Test
	public void validate() throws Exception {
		Response response = client.validate("63D31762A50F937B535746C9E31FA33E");
		Validation data = (Validation) response.getResult().getData();
		logger.debug("response=" + response);
		assertTrue(data.isValidation());
	}
}
