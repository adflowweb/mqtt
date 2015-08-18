/*
 * 
 */
package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.request.AuthReq;
import kr.co.adflow.pms.core.response.AuthRes;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
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
public class AuthServiceImpl implements AuthService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AuthServiceImpl.class);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.CommonService#authUser(kr.co.adflow.pms.
	 * adm.request.AuthReq)
	 */
	@Override
	public AuthRes authUser(AuthReq auth) throws Exception {

		AuthRes res = null;
		res = new AuthRes();
		User paramUser = new User();
		paramUser.setUserId(auth.getUserId());
		paramUser.setPassword(this.getPassword(auth));
		User user = userMapper.selectAuth(paramUser);
		if (user == null) {

			throw new PmsRuntimeException("invalid auth");
		}

		Token paramToken = new Token();
		paramToken.setUserId(user.getUserId());

		Token rst = tokenMapper.getLatest(paramToken);
		// 토큰이 존재
		if (rst != null) {
			logger.debug("이미 토큰이 존재함!");

			res.setToken(rst.getTokenId());
			// 새로운 토큰 발급
		} else {
			logger.debug("새로운 토큰 발급");
			paramToken.setTokenId(KeyGenerator.generateToken(auth.getUserId()));
			int cnt = tokenMapper.insertToken(paramToken);
			res.setToken(paramToken.getTokenId());
			if (cnt < 1) {

				throw new PmsRuntimeException("invalid auth error");
			}
		}

		res.setUserId(user.getUserId());
		res.setUserName(user.getUserName());

		return res;
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
	public boolean authToken(String token) throws Exception {
		logger.debug("validate 시작(token=" + token + ")");
		Token tokenData = tokenMapper.selectToken(token);

		if (tokenData != null) {
			logger.debug("validate 종료(true)");

			// lastaAccess Time update 추가??
			return true;
		} else {
			logger.debug("validate 종료(false)");
			return false;
		}
	}

}
