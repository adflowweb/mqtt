/*
 * 
 */
package kr.co.adflow.push.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.dao.TokenDao;
import kr.co.adflow.push.dao.UserDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.handler.MqttSessionCleanClient;
import kr.co.adflow.push.handler.RestTemplateErrorHandler;
import kr.co.adflow.push.service.TokenService;
import kr.co.adflow.util.TokenGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractTokenServiceImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
//

@Service
abstract public class AbstractTokenServiceImpl implements TokenService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractTokenServiceImpl.class);

	/** The token dao. */
	@Resource
	TokenDao tokenDao;

	/** The user dao. */
	@Resource
	protected UserDao userDao;

	/** The device dao. */
	@Resource
	DeviceDao deviceDao;

	/** The mqtt service impl. */
	@Autowired
	MqttSessionCleanClient mqttServiceImpl;

	@Autowired
	RestTemplateErrorHandler restTemplateErrorHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.TokenService#validate(java.lang.String)
	 */
	@Override
	public boolean validate(String token) throws Exception {
		logger.debug("validate시작(token=" + token + ")");

		Token data = tokenDao.get(token);
		if (data != null) {
			logger.debug("validate종료(true)");
			tokenDao.putLastAcessTime(data);
			return true;
		} else {
			logger.debug("validate종료(false)");
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.TokenService#get(java.lang.String)
	 */
	@Override
	public Token get(String token) throws Exception {
		logger.debug("get시작(token=" + token + ")");
		Token data = tokenDao.get(token);
		// TokenResponse res = new TokenResponse(data.getTokenID(),
		// data.getUserID(), data.getDeviceID());
		logger.debug("get종료(token=" + data + ")");
		return data;
	}

	/*
	 * 토큰 발행하기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.TokenService#post(kr.co.adflow.push.domain.
	 * Token)
	 */
	// @Transactional
	@Override
	public Token post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");

		// insert user
		// User user = new User();
		// user.setUserID(token.getUserID());
		try {
			userDao.post(user);
		} catch (DuplicateKeyException e) {
			logger.debug("유저가이미등록되어있습니다.user=" + user.getUserID());
		}

		// TODO
		if (user.getUfmi() != null && user.getUfmi().trim().length() > 0) {
			userDao.put(user);
		}

		// insert device
		Device device = new Device();
		device.setDeviceID(user.getDeviceID());
		device.setUserID(user.getUserID());

		// KTP-skip-start
		// device.setApnsToken(user.getApnsToken());
		// KTP-skip-end

		// ktp device info skip 20150120
		// try {
		// deviceDao.post(device);
		// } catch (DuplicateKeyException e) {
		// logger.debug("디바이스가이미등록되어있습니다.device=" + device.getDeviceID());
		// }

		// select 최신 token
		// select * from token where userid='kicho' and deviceid='test' order by
		// issue desc limit 1;
		Token token = new Token();
		token.setUserID(user.getUserID());

		// KTP-skip-start
		// token.setDeviceID(user.getDeviceID());
		// KTP-skip-end

		Token rst = tokenDao.getLatest(token);

		// 발급시간에 따른 토큰 재발급로직... 추가해야함

		if (rst != null) {
			// 존재하면 발급시간 업데이트
		} else {
			// 없으면 인서트
			// generate tokenID
			String tokenID = TokenGenerator.generate();
			logger.debug("tokenID=" + tokenID);
			token.setTokenID(tokenID);

			rst = tokenDao.post(token);
		}

		logger.debug("post종료(token=" + rst + ")");
		return rst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.TokenService#put(kr.co.adflow.push.domain.
	 * Token)
	 */
	@Override
	public int put(Token token) throws Exception {
		return tokenDao.put(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.TokenService#delete(java.lang.String)
	 */
	@Override
	public int delete(String token) throws Exception {
		logger.debug("delete시작(token=" + token + ")");

		// TODO:1.provisioning 서버에서 해당토큰이 삭제됨을 알려준다
		this.notifyProvisioning(token);

		// TODO:2.MQTT Client로 Connection 을 연결후 종료한다.(clean Session true)

		String[] serverUrl = MqttSessionCleanClient.SERVERURL;
		for (int i = 0; i < serverUrl.length; i++) {
			mqttServiceImpl.mqttConnectionClean(token, serverUrl[i]);
		}

		logger.debug("연결성공 숫자:" + MqttSessionCleanClient.MQCONNCOUNT);
		int count = 0;
		int result = serverUrl.length / 2;
		logger.debug("result:" + result);
		if (MqttSessionCleanClient.MQCONNCOUNT == result) {
			MqttSessionCleanClient.MQCONNCOUNT = 0;

			logger.debug("토큰삭제 시작:" + token);
			count = tokenDao.delete(token);
			logger.debug("delete종료(updates=" + count + ")");

		} else {
			logger.debug("MQTT client 연결중 Exception 발생");
			throw new Exception();
		}

		return count;
	}

	// update : 140901 <kicho> - start
	/*
	 * user의 Tokens 가져오기 (non-Javadoc)
	 * 
	 */
	@Override
	public Token[] getByUser(String userID) throws Exception {
		logger.debug("getByUser시작(userID=" + userID + ")");

		Token[] tokens = tokenDao.getByUser(userID);
		logger.debug("getByUser종료(tokens=" + tokens + ")");
		return tokens;
	}
	// update : 140901 <kicho> - end

	// update : 140902 <kicho> - start
	/*
	 * user의 Tokens 가져오기 (non-Javadoc)
	 * 
	 */
	@Override
	public Token[] getMultiByUser(String userID) throws Exception {
		logger.debug("getMultiByUser시작(userID=" + userID + ")");

		Token[] tokens = tokenDao.getMultiByUser(userID);
		logger.debug("getMultiByUser종료(tokens=" + tokens + ")");
		return tokens;
	}
	// update : 140902 <kicho> - end

	public Token[] getMultiByUfmi(String ufmi) throws Exception {

		Token[] tokens = tokenDao.getMultiByUfmi(ufmi);

		return tokens;
	}

	public void notifyProvisioning(String token) throws Exception {

		Token tokenResult = tokenDao.get(token);
		int responseCode = 0;
		ResponseEntity<String> response = null;
		ClientHttpResponse clientHttpResponse = null;
		try {
			RestTemplate proRestTemplate = new RestTemplate();
			proRestTemplate.setErrorHandler(restTemplateErrorHandler);
			clientHttpResponse = restTemplateErrorHandler.getClientHttpResponse();
			String proUrl = MqttSessionCleanClient.PROVISIONINGURL + "/" + tokenResult.getUserID() + "?token=123456789";
			logger.debug("요청 url:" + proUrl);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Content-Type", "application/json");
			headers.add("accept-version", "1.0.0");
			HttpEntity entity = new HttpEntity(headers);
			response = proRestTemplate.exchange(proUrl, HttpMethod.DELETE, entity, String.class);
			HttpStatus httpStatus = response.getStatusCode();
			responseCode = httpStatus.value();
			logger.debug("삭제요청 함 ");
			if (responseCode == 200) {
				logger.debug("정상응답");
				logger.debug("Provisioning server token 삭제 완료");
			} else if (responseCode == 404) {
				logger.debug("해당 토큰 DATA가 Provsioning 서버에 존재하지 않음 정상삭제처리");
			} else {
				logger.debug("요청에라 예외발생");
				throw new Exception();
			}

		} catch (HttpClientErrorException e) {
			logger.debug("Provisioning Server 로 Rest 요청중에 error 발생");
			responseCode = clientHttpResponse.getStatusCode().value();
			logger.debug("response Code:" + responseCode);
			logger.debug("요청 에라");
			throw new Exception();

		}

	}

}
