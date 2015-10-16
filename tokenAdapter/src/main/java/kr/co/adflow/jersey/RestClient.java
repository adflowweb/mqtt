package kr.co.adflow.jersey;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import kr.co.adflow.push.domain.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RestClient {

	private static Properties prop = new Properties();
	private static final Logger logger = LoggerFactory
			.getLogger(RestClient.class);

	static {
		try {
			prop.load(RestClient.class
					.getResourceAsStream("/config.properties"));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final String API_KEY = prop.getProperty("api.key");

	protected Client client;
	protected WebResource webResource;

	public RestClient() {
		logger.debug("RestClient생성자시작()");
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
		webResource = client.resource(
				"http://" + prop.getProperty("server.ip") + ":"
						+ prop.getProperty("server.port")).path(
				prop.getProperty("api.version"));
		logger.debug("webResource=" + webResource);
		logger.debug("RestClient생성자종료()");
	}

	public String validate(String token) {
		logger.debug("validate시작(token=" + token + ")");
		logger.debug("webResource=" + webResource);
		String valid = null;
		try {
			logger.debug("api.key=" + API_KEY);
			Response data = webResource.path("auth").path(token)
					.accept(MediaType.APPLICATION_JSON)
					.header("X-Application-Key", API_KEY)
					.get(new GenericType<Response>() {
					});
			logger.debug("data=" + data + ")");
			valid = data.getStatus();
			logger.debug("validate종료(valid=" + valid + ")");
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
		return valid;
	}

	public WebResource getWebResource() {
		return webResource;
	}

}
