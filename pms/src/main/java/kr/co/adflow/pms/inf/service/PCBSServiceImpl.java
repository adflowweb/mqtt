package kr.co.adflow.pms.inf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.inf.request.UserReq;

@Service
public class PCBSServiceImpl implements PCBSService {
	
	
	private static final String SERVICE_USER_ROLE = "svc";
	private static final String APPLICATION_TOKEN_TYPE = "A";
	private static final String DEFAULT_IP_FILTER = "0.0.0.0";
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TokenMapper tokenMapper;

	@Override
	public String addUser(UserReq userReq) {

		User user = new User();
		user.setUserId(userReq.getUserId());
		user.setPassword(this.getPassword(userReq));
		user.setRole(SERVICE_USER_ROLE);
		user.setIpFilters(DEFAULT_IP_FILTER);
		user.setMsgCntLimitDay(userReq.getMsgCntLimitDay());
		user.setStatus(-1);
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(APPLICATION_TOKEN_TYPE);
		token.setTokenId(this.getTokenId(userReq));
		
		userMapper.insertUser(user);
		tokenMapper.insertToken(token);
		user.setStatus(0);
		userMapper.updateUserStatus(user);
		//
		//userMapper udate
		
		return user.getUserId();
	}
	
	private String getPassword(UserReq req) {
		
		return KeyGenerator.generateHash(req.getPassword());
	}
	
	private String getTokenId(UserReq req) {
		
		return KeyGenerator.generateToken(req.getUserId());
	}

}
