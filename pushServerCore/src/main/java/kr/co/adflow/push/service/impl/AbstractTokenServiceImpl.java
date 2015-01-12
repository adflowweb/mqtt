package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.dao.TokenDao;
import kr.co.adflow.push.dao.UserDao;
import kr.co.adflow.push.dao.impl.DeviceDaoImpl;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.TokenService;
import kr.co.adflow.util.TokenGenerator;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
// @Service
abstract public class AbstractTokenServiceImpl implements TokenService {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AbstractTokenServiceImpl.class);

	@Resource
	TokenDao tokenDao;
	
	@Resource
	protected UserDao userDao;

	@Resource
	DeviceDao deviceDao;
	
	@Autowired
	AbstractMqttServiceImpl mqttServiceImpl;

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
		
		//TODO
		if (user.getUfmi() != null && user.getUfmi().trim().length() > 0) {
			userDao.put(user);
		}

		// insert device
		Device device = new Device();
		device.setDeviceID(user.getDeviceID());
		device.setUserID(user.getUserID());
		
		//KTP-skip-start
//		device.setApnsToken(user.getApnsToken());
		//KTP-skip-end
		
		
		try {
			deviceDao.post(device);
		} catch (DuplicateKeyException e) {
			logger.debug("디바이스가이미등록되어있습니다.device=" + device.getDeviceID());
		}

		// select 최신 token
		// select * from token where userid='kicho' and deviceid='test' order by
		// issue desc limit 1;
		Token token = new Token();
		token.setUserID(user.getUserID());
		
		//KTP-skip-start
//		token.setDeviceID(user.getDeviceID());
		//KTP-skip-end
		
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

	@Override
	public int put(Token token) throws Exception {
		return tokenDao.put(token);
	}

	@Override
	public int delete(String token) throws Exception {
		logger.debug("delete시작(token=" + token + ")");
		
		
		//140829 - 토큰 삭제시 해당 토큰 cleansession = true 로 conneciton 후 disconnection 시도 - start
		mqttServiceImpl.mqttConnectionClean(token);
		//140829 - 토큰 삭제시 해당 토큰 cleansession = true 로 conneciton 후 disconnection 시도 - end
		
		int count = tokenDao.delete(token);
		logger.debug("delete종료(updates=" + count + ")");
		
		return count;
	}
	

	 // update : 140901 <kicho> - start
	/*
	 * user의 Tokens 가져오기 
	 * (non-Javadoc)
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
	 * user의 Tokens 가져오기 
	 * (non-Javadoc)
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

}
