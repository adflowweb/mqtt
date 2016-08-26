/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.handler.ZookeeperHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.AppKey;
import kr.co.adflow.pms.domain.Leader;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonServiceImpl.
 */
@Service
public class CommonServiceImpl implements CommonService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The token mapper. */
	@Autowired
	private TokenMapper tokenMapper;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	@Autowired
	private ZookeeperHandler zookeeperHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.CommonService#authUser(kr.co.adflow.pms.adm.
	 * request.AuthReq)
	 */
	@Override
	public AuthRes authUser(AuthReq auth) throws Exception {

		AuthRes res = null;
		res = new AuthRes();
		User paramUser = new User();
		paramUser.setUserId(auth.getUserId());
		// 1. password hashing
		paramUser.setPassword(this.getPassword(auth));
		// 2. userId/password 로 조회
		User user = userMapper.selectAuth(paramUser);
		if (user == null) {
			// error
			// throw new RuntimeException("invalid auth");
			throw new PmsRuntimeException("invalid auth");
		}

		if (auth.getType() != null && auth.getType().equals("svc")) {
			// 서드파티 관제 유저는 application Key 리턴.
			String appKey = tokenMapper.selectApplicationKey(user.getUserId());
			res.setToken(appKey);
		} else {
			// 일반 관제 유저는 application Token 리턴.
			Token paramToken = new Token();
			paramToken.setUserId(user.getUserId());
			paramToken.setTokenType(StaticConfig.TOKEN_TYPE_TOKEN);
			// 3. 정상 사용자의 경우 token 해싱
			paramToken.setTokenId(KeyGenerator.generateToken(auth.getUserId()));
			// 4 , expired_time 현재로 부터 30분
			paramToken.setExpiredTime(
					DateUtil.afterMinute(pmsConfig.HEADER_APPLICATION_TOKEN_EXPIRED, System.currentTimeMillis()));
			// 5. token 테이블 저장
			int cnt = tokenMapper.insertToken(paramToken);
			if (cnt < 1) {
				// throw new RuntimeException("invalid auth error");
				throw new PmsRuntimeException("invalid auth error");
			}
			// 6. 만료일 지난 token 삭제
			tokenMapper.deleteExpiredToken(paramToken.getUserId());

			res.setToken(paramToken.getTokenId());

		}

		res.setUserId(user.getUserId());
		res.setRole(user.getRole());
		res.setUfmi(user.getUfmi());
		res.setUserName(user.getUserName());
		if (user.getGroupTopics() != null && user.getGroupTopics().trim().length() > 0) {
			res.setGroupTopics(user.getGroupTopics().split(","));
		} else {
			res.setGroupTopics(null);
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.CommonService#authUser(kr.co.adflow.pms.adm.
	 * request.AuthReq)
	 */
	@Override
	public boolean authToken(String token) throws Exception {

		boolean result = false;

		AppKey tokenKey = new AppKey();

		tokenKey.setApplicationKey(token);
		// tokenKey.setRole(StaticConfig.USER_ROLE_SERVICE);

		// Token Check
		Date expiredTime = interceptMapper.selectCashedApplicationTokenCmm(tokenKey);

		if (expiredTime != null) {
			result = true;
		} else {
			// AppKey Check
			String userId = interceptMapper.selectCashedApplicationKeyCmm(tokenKey);

			if (userId != null) {
				result = true;
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.CommonService#authUser(kr.co.adflow.pms.adm.
	 * request.AuthReq)
	 */
	@Override
	public boolean authKey(String key) throws Exception {

		boolean result = false;

		AppKey appKey = new AppKey();

		appKey.setApplicationKey(key);
		// tokenKey.setRole(StaticConfig.USER_ROLE_SERVICE);

		String userId = interceptMapper.selectCashedApplicationKeyCmm(appKey);

		if (userId != null) {
			result = true;
		}

		return result;
	}

	/**
	 * Gets the password.
	 *
	 * @param req
	 *            the req
	 * @return the password
	 */
	private String getPassword(AuthReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	@Override
	public Leader getLeader() throws Exception {
		// TODO Auto-generated method stub
		Leader leader = new Leader();
		boolean leaderCheck = zookeeperHandler.getLeader();
		leader.setLeaderCheck(leaderCheck);
		leader.setServerId(pmsConfig.EXECUTOR_SERVER_OLD_PMS_ID);

		return leader;
	}

}
