package kr.co.adflow.pms.adm.service;

import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.controller.CommonController;
import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

@Service
public class CommonServiceImpl implements CommonService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(CommonServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TokenMapper tokenMapper;
	
	@Autowired
	private PmsConfig pmsConfig;

	
	@Override
	public AuthRes authUser(AuthReq auth) {

		AuthRes res = null;
		res = new AuthRes();
		User paramUser = new User();
		paramUser.setUserId(auth.getUserId());
		// 1. password hashing
		paramUser.setPassword(this.getPassword(auth));
		// 2. userId/password 로 조회
		User user = userMapper.selectAuth(paramUser);
		if (user == null) {
			//error
			throw new RuntimeException("invalid auth");
		} 
		
		Token paramToken = new Token();
		paramToken.setUserId(user.getUserId());
		paramToken.setTokenType(StaticConfig.TOKEN_TYPE_TOKEN);
		// 3. 정상 사용자의 경우 token 해싱
		paramToken.setTokenId(KeyGenerator.generateToken(auth.getUserId()));
		// 4 , expired_time 현재로 부터 30분
		paramToken.setExpiredTime(DateUtil.afterMinute(pmsConfig.HEADER_APPLICATION_TOKEN_EXPIRED, System.currentTimeMillis()));
		// 5. token 테이블 저장
		int cnt = tokenMapper.insertToken(paramToken);
		if (cnt < 1) {
			throw new RuntimeException("invalid auth error");
		}
		// 6. 만료일 지난 token 삭제
		tokenMapper.deleteExpiredToken(paramToken.getUserId());
		
		res.setToken(paramToken.getTokenId());
		res.setUserId(user.getUserId());
		res.setRole(user.getRole());

		return res;
	}
	
	private String getPassword(AuthReq req) {

		return KeyGenerator.createPw(req.getUserId(),req.getPassword());
	}

}
