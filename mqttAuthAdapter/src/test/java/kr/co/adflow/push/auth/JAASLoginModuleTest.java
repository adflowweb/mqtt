package kr.co.adflow.push.auth;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JAASLoginModuleTest {

	private static final Logger logger = LoggerFactory
			.getLogger(JAASLoginModuleTest.class);

	@BeforeClass
	void bfterclass() throws Exception {

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
}
