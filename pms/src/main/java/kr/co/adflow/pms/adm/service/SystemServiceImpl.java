package kr.co.adflow.pms.adm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TokenMapper tokenMapper;
	@Autowired
	private InterceptMapper interceptMapper;
	
	@Override
	public List<User> listAllUser() {
		
		return userMapper.listAll();
	}

	@Override
	public String createUser(UserReq userReq,String appKey) {
		
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User user = new User();
		user.setUserId(userReq.getUserId());
		user.setUserName(userReq.getUserName());
		user.setPassword(this.getPassword(userReq));
		user.setRole(userReq.getRole());
		user.setIssueId(issueId);
		
		if (userReq.getIpFilters() == null || userReq.getIpFilters().trim().length() == 0) {
			user.setIpFilters(PmsConfig.INTERCEPTER_IP_FILTER);
		} else {
			user.setIpFilters(userReq.getIpFilters());
		}
		
		user.setMsgCntLimit(userReq.getMsgCntLimit());
		user.setStatus(-1);
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(PmsConfig.TOKEN_TYPE_APPLICATION);
		token.setTokenId(this.getTokenId(userReq));

		//
		user.setAction("createUser");
		userMapper.logUserHistory(user);
		
		userMapper.insertUser(user);
		tokenMapper.insertToken(token);
		user.setStatus(PmsConfig.USER_STATUS_NORMAL);
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

	@Override
	public User retrieveUser(String userId) {
		return userMapper.select(userId);
	}

	@Override
	public int updateUser(UserReq userReq,String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User param = userMapper.select(userReq.getUserId());
		
		if (userReq.getUserName() != null)
		param.setUserName(userReq.getUserName());
		
		if (userReq.getMsgCntLimit() != 0)
		param.setMsgCntLimit(userReq.getMsgCntLimit());
		
		if (userReq.getRole() != null)
		param.setRole(userReq.getRole());
		
		//TODO validation 필요?
		if (userReq.getIpFilters() != null)
		param.setIpFilters(userReq.getIpFilters());
		
		
		param.setStatus(PmsConfig.USER_STATUS_NORMAL);
		param.setIssueId(issueId);
		
		param.setAction("updateUser");
		userMapper.logUserHistory(param);

		return userMapper.updateUser(param);
	}

	@Override
	public int deleteUser(String userId,String appKey) {
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User param = userMapper.select(userId);
		param.setIssueId(issueId);
		param.setAction("deleteUser");
		userMapper.logUserHistory(param);
		tokenMapper.deleteUserToken(userId);
		return userMapper.deleteUser(userId);
	}

	
	

}
