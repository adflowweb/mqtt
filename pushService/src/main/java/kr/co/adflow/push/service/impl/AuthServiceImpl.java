package kr.co.adflow.push.service.impl;

import kr.co.adflow.push.domain.AuthResponseData;
import kr.co.adflow.push.service.AuthService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * 
 */
@Service
public class AuthServiceImpl implements AuthService {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AuthServiceImpl.class);

	@Override
	public AuthResponseData authencate(String tocken) throws Exception {
		logger.debug("authencate시작(tocken=" + tocken + ")");
		// todo 인증구현
		// 디비에 토큰이 존재하는지
		// userID 와 clientID가 존재하는지
		AuthResponseData res = new AuthResponseData(true, "testUser");
		logger.debug("authencate종료()");
		return res;
	}
}
