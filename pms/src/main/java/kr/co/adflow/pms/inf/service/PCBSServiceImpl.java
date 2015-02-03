package kr.co.adflow.pms.inf.service;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
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
	@Autowired
	private InterceptMapper interceptMapper;

	@Override
	public String addUser(UserReq userReq,String appKey) {
		
		String issueId = interceptMapper.selectCashedUserId(appKey);

		User user = new User();
		user.setUserId(userReq.getUserId());
		user.setPassword(this.getPassword(userReq));
		user.setRole(PmsConfig.USER_ROLE_SERVICE);
		user.setIpFilters(PmsConfig.INTERCEPTER_IP_FILTER);
		user.setMsgCntLimit(userReq.getMsgCntLimit());
		user.setStatus(-1);
		user.setIssueId(issueId);
		
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(PmsConfig.TOKEN_TYPE_APPLICATION);
		token.setTokenId(this.getTokenId(userReq));
		token.setIssueId(issueId);

		user.setAction("addUser");
		userMapper.logUserHistory(user);
		userMapper.insertUser(user);
		tokenMapper.insertToken(token);
		user.setStatus(0);
		userMapper.updateUserStatus(user);
		//
		// userMapper udate

		return user.getUserId();
	}

	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(),req.getPassword());
	}

	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

}
