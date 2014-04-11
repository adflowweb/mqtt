package kr.co.adflow.push.auth;

import java.io.IOException;
import java.util.Properties;

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
	public void loadPropertiesFile() {
		Properties prop = new Properties();
		try {
			prop.load(JAASLoginModule.class
					.getResourceAsStream("/config.properties"));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getAuth() {
		try {
			Response response = client.getAuth("testTocken");
			logger.debug("response=" + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
