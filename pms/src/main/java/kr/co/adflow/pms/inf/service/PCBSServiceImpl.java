package kr.co.adflow.pms.inf.service;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.inf.request.UserReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PCBSServiceImpl implements PCBSService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TokenMapper tokenMapper;

	@Override
	public String addUser(UserReq userReq) {

		User user = new User();
		user.setUserId(userReq.getUserId());
		user.setPassword(this.getPassword(userReq));
		user.setRole(PmsConfig.USER_ROLE_SERVICE);
		user.setIpFilters(PmsConfig.INTERCEPTER_IP_FILTER);
		user.setMsgCntLimitDay(userReq.getMsgCntLimitDay());
		user.setStatus(-1);
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(PmsConfig.HEADER_APPLICATION_KEY);
		token.setTokenId(this.getTokenId(userReq));

		userMapper.insertUser(user);
		tokenMapper.insertToken(token);
		user.setStatus(0);
		userMapper.updateUserStatus(user);
		//
		// userMapper udate

		return user.getUserId();
	}

	private String getPassword(UserReq req) {

		return KeyGenerator.generateHash(req.getPassword());
	}

	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

}
