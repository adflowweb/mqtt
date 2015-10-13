package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.request.UserUpdateReq;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory
			.getLogger(PCFServiceImpl.class);

	@Autowired
	private CheckUtil checkUtil;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private InterceptMapper interceptMapper;

	@Autowired
	private TokenMapper tokenMapper;

	@Override
	public String createUser(UserReq userReq) throws Exception {

		User paramUser = new User();
		paramUser.setUserId(userReq.getUserId());
		paramUser.setUserName(userReq.getUserName());
		paramUser.setPassword(this.getPassword(userReq));
		paramUser.setRole("admin");

		paramUser.setStatus(-1);

		Token paramToken = new Token();
		paramToken.setUserId(paramUser.getUserId());
		//
		paramUser.setAction("createUser");
		userMapper.logUserHistory(paramUser);

		try {
			userMapper.insertUser(paramUser);

		} catch (DuplicateKeyException e) {
			logger.debug("유저가 이미 등록되어 있습니다. user=" + paramUser.getUserId());
		}

		paramUser.setStatus(StaticConfig.USER_STATUS_NORMAL);

		userMapper.updateUserStatus(paramUser);

		paramToken
				.setTokenId(KeyGenerator.generateToken(paramUser.getUserId()));
		int cnt = tokenMapper.insertToken(paramToken);

		if (cnt < 1) {

			throw new PmsRuntimeException("invalid auth error");
		}

		return paramUser.getUserId();
	}

	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	@Override
	public int updateUser(UserUpdateReq userReq, String appKey)
			throws Exception {
		String issueId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = userMapper.select(userReq.getUserId());

		if (userReq.getUserName() != null)
			paramUser.setUserName(userReq.getUserName());
		paramUser.setStatus(StaticConfig.USER_STATUS_NORMAL);
		paramUser.setIssueId(issueId);

		paramUser.setAction("updateUser");
		userMapper.logUserHistory(paramUser);

		return userMapper.updateUser(paramUser);
	}

	@Override
	public User retrieveUser(String userId) throws Exception {
		User resultUser = null;
		resultUser = userMapper.select(userId);

		resultUser.setDefaultExpiry(checkUtil.getMessageExpiry(resultUser
				.getDefaultExpiry()));
		resultUser.setDefaultQos(checkUtil.getMessageQos(resultUser
				.getDefaultQos()));

		return resultUser;
	}

	@Override
	public int deleteUser(String userId, String appKey) throws Exception {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User param = userMapper.select(userId);
		param.setIssueId(issueId);
		param.setAction("deleteUser");
		userMapper.logUserHistory(param);
		tokenMapper.deleteUserToken(userId);
		return userMapper.deleteUser(userId);
	}

}
