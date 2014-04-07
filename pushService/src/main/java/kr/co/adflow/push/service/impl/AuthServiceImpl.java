package kr.co.adflow.push.service.impl;

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
	public boolean authencate(String userID, String clientID) throws Exception {
		logger.debug("authencate시작(userID=" + userID + "|clientID=" + clientID
				+ ")");
		// todo 인증구현
		// 디비에 userID 와 clientID가 존재하는지
		logger.debug("authencate종료()");
		return true;
	}
}
