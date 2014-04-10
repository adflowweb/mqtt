package kr.co.adflow.push.auth;

import kr.co.adflow.jersey.RestClient;
import kr.co.adflow.push.domain.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestClientTest {
	private static final Logger logger = LoggerFactory
			.getLogger(RestClientTest.class);

	RestClient client;

	@BeforeClass
	void bfterclass() throws Exception {
		client = new RestClient();
	}

	@Test
	public void getAuth() {
		Response response = client.getAuth("testTocken");
		logger.debug("response=" + response);
	}
}
