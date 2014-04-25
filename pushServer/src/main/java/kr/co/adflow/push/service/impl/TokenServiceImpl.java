package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.TokenDAO;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.service.TokenService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class TokenServiceImpl implements TokenService {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenServiceImpl.class);

	@Resource
	TokenDAO tokenDao;

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

	@Override
	public void post(Token token) throws Exception {
		tokenDao.post(token);
	}

	@Override
	public void put(Token token) throws Exception {
		tokenDao.put(token);
	}

	@Override
	public void delete(String token) throws Exception {
		tokenDao.delete(token);
	}

}
