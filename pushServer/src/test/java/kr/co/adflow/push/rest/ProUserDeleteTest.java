package kr.co.adflow.push.rest;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class ProUserDeleteTest {
	CloseableHttpClient closeableHttpClient = null;
	PoolingHttpClientConnectionManager poolingConnectionManager = null;
	HttpClientBuilder httpClientBulider = null;
	HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = null;
	RestTemplate restTemplate = null;
	List<HttpMessageConverter<?>> messageConverters = null;

	@BeforeClass
	void createConnectionPool() {
		// this.poolingConnectionManager = new
		// PoolingHttpClientConnectionManager();
		// this.poolingConnectionManager.setMaxTotal(20);
		// this.poolingConnectionManager.setDefaultMaxPerRoute(20);
		// this.httpClientBulider =
		// HttpClients.custom().setConnectionManager(poolingConnectionManager);
		// this.closeableHttpClient = this.httpClientBulider.build();
		// this.clientHttpRequestFactory = new
		// HttpComponentsClientHttpRequestFactory(closeableHttpClient);
		// this.restTemplate = new RestTemplate();
		// // this.messageConverters = new ArrayList<HttpMessageConverter<?>>();
		// // messageConverters.add(new FormHttpMessageConverter());
		// // messageConverters.add(new StringHttpMessageConverter());
		// // messageConverters.add(new MappingJacksonHttpMessageConverter());
		// // this.restTemplate.setMessageConverters(messageConverters);
		// this.restTemplate.setRequestFactory(clientHttpRequestFactory);

	}

	// @Test(priority = 1)
	// void getUser() throws JSONException {
	// // String url = "http://192.168.0.79:8080/token/pcs/1234567890";
	// // long startTime = System.currentTimeMillis();
	// // for (int i = 0; i < 1000; i++) {
	// //
	// // String result = this.restTemplate.getForObject(url, String.class);
	// // // System.out.println(server.getServer());
	// // // System.out.println(server.getUser());
	// //
	// // // System.out.println(server.getIssuetime());
	// // // System.out.println(result);
	// // // JSONObject jsonObject = new JSONObject(result);
	// // //
	// // // String server = jsonObject.getString("server");
	// // // System.out.println(server);
	// // }
	// // long stopTime = System.currentTimeMillis();
	// // long resultTime = stopTime - startTime;
	// // System.out.println("걸린시간:" + resultTime + "ms");
	//
	// }

	@Test(priority = 1)
	void getUserNewTemplate() throws Exception {
		// http://14.63.217.141:8083/user/:id
		System.out.println("테스트 시작");
		RestTemplate newRestTemplate = new RestTemplate();
		String url = "http://14.63.217.141:38083/user/+821027547219?token=123456789";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.add("accept-version", "1.0.0");
		HttpEntity entity = new HttpEntity(headers);
		long startTime = System.currentTimeMillis();
		ResponseEntity<String> response = newRestTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
		HttpStatus httpStatus = response.getStatusCode();
		System.out.println(httpStatus.value());
		org.testng.Assert.assertEquals(httpStatus.value(), 200);
		long stopTime = System.currentTimeMillis();
		long resultTime = stopTime - startTime;
		System.out.println("걸린시간:" + resultTime + "ms");
		// }

	}

}
