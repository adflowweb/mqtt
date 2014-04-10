package kr.co.adflow.jersey;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import kr.co.adflow.push.domain.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
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

	protected Client client;
	protected WebResource webResource;

	public RestClient() {
		logger.debug("RestClient생성자시작()");
		logger.debug("properties=" + prop);
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
		webResource = client.resource(
				"http://" + prop.getProperty("server.ip") + ":"
						+ prop.getProperty("server.port")).path("push");
		logger.debug("webResource=" + webResource);
		logger.debug("RestClient생성자종료()");
	}

	public Response getAuth(String tocken) {
		logger.debug("getAuth시작(tocken=" + tocken + ")");
		Response data = webResource.path("auth").path("tockens").path(tocken)
				.accept(MediaType.APPLICATION_JSON).get(Response.class);
		// .accept(MediaType.APPLICATION_JSON_TYPE)
		logger.debug("getAuth종료()");
		return data;
	}
}
