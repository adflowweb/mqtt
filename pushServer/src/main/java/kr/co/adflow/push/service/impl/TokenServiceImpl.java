package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.TokenDAO;
import kr.co.adflow.push.domain.ResponseData;
import kr.co.adflow.push.domain.TokenResponseData;
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
	public ResponseData validate(String token) throws Exception {
		logger.debug("validate시작(token=" + token + ")");
		// todo 인증구현
		// 디비에 토큰이 존재하는지
		// userID 와 clientID가 존재하는지
		TokenResponseData res = new TokenResponseData(tokenDao.validate(token),
				"testUser");
		logger.debug("validate종료()");
		return res;
	}

	@Override
	public ResponseData get(String token) throws Exception {
		return null;
	}

	@Override
	public ResponseData post(String token) throws Exception {
		return null;
	}

	@Override
	public ResponseData put(String token) throws Exception {
		return null;
	}

	@Override
	public ResponseData delete(String token) throws Exception {
		return null;
	}
}
