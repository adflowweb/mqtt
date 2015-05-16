/*
 * 
 */
package kr.co.adflow.pms.inf.service;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class PCBSServiceImpl.
 */
@Service
public class PCBSServiceImpl implements PCBSService {

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;
	
	/** The token mapper. */
	@Autowired
	private TokenMapper tokenMapper;
	
	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.inf.service.PCBSService#addUser(kr.co.adflow.pms.inf.request.UserReq, java.lang.String)
	 */
	@Override
	public String addUser(UserReq userReq, String issueId) {
		
		User user = userMapper.select(userReq.getUserId());
		if (user == null || user.getUserId() == null || user.getUserId().trim().length() == 0) {
			// Add User
			user = new User();
			user.setUserId(userReq.getUserId());
			user.setPassword(this.getPassword(userReq));
			user.setRole(StaticConfig.USER_ROLE_SERVICE);
			user.setIpFilters(StaticConfig.INTERCEPTER_IP_FILTER);
			user.setMsgCntLimit(userReq.getMsgCntLimit());
			user.setStatus(-1);
			user.setIssueId(issueId);
			user.setUfmi(userReq.getUfmi());
			user.setSaId(userReq.getSaId());
//			user.setGroupTopics(userReq.getGroupTopics());

			Token token = new Token();
			token.setUserId(userReq.getUserId());
			token.setTokenType(StaticConfig.TOKEN_TYPE_APPLICATION);
			token.setTokenId(this.getTokenId(userReq));
			token.setIssueId(issueId);

			user.setAction("addUser");
			userMapper.logUserHistory(user);
			userMapper.insertUser(user);
			tokenMapper.insertToken(token);
			user.setStatus(userReq.getStatus());
			userMapper.updateUserStatus(user);
			
		} else {
			//Update user
			user.setPassword(this.getPassword(userReq));
			user.setRole(StaticConfig.USER_ROLE_SERVICE);
			user.setIpFilters(StaticConfig.INTERCEPTER_IP_FILTER);
			user.setMsgCntLimit(userReq.getMsgCntLimit());
			user.setStatus(userReq.getStatus());
			user.setIssueId(issueId);
			user.setUfmi(userReq.getUfmi());
			user.setSaId(userReq.getSaId());
//			user.setGroupTopics(userReq.getGroupTopics());
			
			user.setAction("updateUser");
			userMapper.logUserHistory(user);
			userMapper.updateUser(user);

		}

		return user.getUserId();
	}

	/**
	 * Gets the password.
	 *
	 * @param req the req
	 * @return the password
	 */
	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	/**
	 * Gets the token id.
	 *
	 * @param req the req
	 * @return the token id
	 */
	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.inf.service.PCBSService#retrieveUser(java.lang.String)
	 */
	@Override
	public User retrieveUser(String userId) {
		return userMapper.select(userId);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.inf.service.PCBSService#updateUser(kr.co.adflow.pms.inf.request.UserUpdateReq, java.lang.String)
	 */
	@Override
	public int updateUser(UserUpdateReq userReq, String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User param = userMapper.select(userReq.getUserId());

		if (userReq.getMsgCntLimit() != 0)
			param.setMsgCntLimit(userReq.getMsgCntLimit());

		param.setStatus(StaticConfig.USER_STATUS_NORMAL);
		param.setIssueId(issueId);

		param.setAction("updateUser");
		userMapper.logUserHistory(param);

		return userMapper.updateUser(param);

	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.inf.service.PCBSService#deleteUser(java.lang.String, java.lang.String)
	 */
	@Override
	public int deleteUser(String userId, String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User param = userMapper.select(userId);
		param.setIssueId(issueId);
		param.setAction("deleteUser");
		userMapper.logUserHistory(param);
		tokenMapper.deleteUserToken(userId);
		return userMapper.deleteUser(userId);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.inf.service.PCBSService#modifyPassword(kr.co.adflow.pms.inf.request.PasswordReq, java.lang.String)
	 */
	@Override
	public int modifyPassword(PasswordReq req, String appKey) {
		if (!req.getNewPassword().trim().equals(req.getRePassword().trim())) {
			throw new RuntimeException("패스워드 변경 실패1");
		}

		// TODO OLD <> NEW 확인 필요?

		String userId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = new User();
		paramUser.setUserId(userId);
		paramUser.setPassword(this.getPassword(userId, req.getOldPassword()));

		User user = userMapper.selectAuth(paramUser);
		if (user == null) {
			throw new RuntimeException("패스워드 변경 실패2");
		}

		paramUser.setAction("modifyPassword");
		paramUser.setPassword(this.getPassword(userId, req.getNewPassword()));
		paramUser.setIssueId(userId);

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updatePassword(paramUser);
		} else {
			throw new RuntimeException("패스워드 변경 실패3");
		}

		return cnt;
	}

	/**
	 * Gets the password.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @return the password
	 */
	private String getPassword(String userId, String password) {

		return KeyGenerator.createPw(userId, password);
	}

}
