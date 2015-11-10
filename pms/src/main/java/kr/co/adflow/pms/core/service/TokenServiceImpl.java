/*
 * 
 */
package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenInfoRes;
import kr.co.adflow.pms.core.response.TokenRes;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Device;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.DeviceMapper;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonServiceImpl.
 */
@Service
public class TokenServiceImpl implements TokenService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(TokenServiceImpl.class);

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
	private DeviceMapper deviceMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.CommonService#authUser(kr.co.adflow.pms.
	 * adm.request.AuthReq)
	 */
	@Override
	public TokenRes createToken(TokenReq userInfo, String requestUserId)
			throws Exception {
		// 유저 생성
		User user = new User();
		user.setUserId(userInfo.getUserId());
		user.setUserName(userInfo.getUserName());
		user.setIssueId(requestUserId);
		user.setStatus(StaticConfig.USER_STATUS_NORMAL);
		user.setGroupCode(StaticConfig.USER_GROUP_CODE);
		user.setAction("Create User");
		user.setServerId(pmsConfig.EXECUTOR_SERVER_ID);

		// history 삭제
		// userMapper.logUserHistory(user);

		try {
			userMapper.insertUser(user);
		} catch (DuplicateKeyException e) {
			logger.debug(user.getUserId() + "는 이미 등록된 유저 입니다.");

		}

		Token paramToken = new Token();
		paramToken.setUserId(userInfo.getUserId());
		paramToken.setDeviceId(userInfo.getDeviceId());
		paramToken.setIssueId(requestUserId);
		TokenRes res = null;
		res = new TokenRes();
		Token rstToken = tokenMapper.getLatest(paramToken);
		// 토큰이 존재
		if (rstToken != null) {
			logger.debug("이미 토큰이 존재함!");
			res.setToken(rstToken.getTokenId());

		} else {
			logger.debug("디바이스 등록 시작");
			Device device = new Device();
			device.setDeviceId(userInfo.getDeviceId());
			device.setDeviceInfo(userInfo.getDeviceInfo());
			device.setUserId(userInfo.getUserId());
			int deviceResult = 0;
			try {
				deviceResult = deviceMapper.insertDevice(device);
			} catch (DuplicateKeyException e) {
				throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510500,
						"이미 등록된 디바이스 입니다");
			}

			if (deviceResult < 1) {
				throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510500,
						"디바이스 등록에 실패 하였습니다");
			}

			logger.debug("디바이스 등록 끝");
			logger.debug("새로운 토큰 발급");
			paramToken.setTokenId(KeyGenerator.generateToken(userInfo
					.getUserId()));
			int tokenResult = tokenMapper.insertToken(paramToken);
			res.setToken(paramToken.getTokenId());
			if (tokenResult < 1) {

				throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510500,
						"토큰 생성에 실패 하였습니다");
			}

			logger.debug("새로운 토큰 발급 끝");
		}

		res.setUserId(user.getUserId());
		res.setServerId(user.getServerId());

		return res;
	}

	/**
	 * Gets the password.
	 * 
	 * @param req
	 *            the req
	 * @return the password
	 */
	private String getPassword(TokenReq req) {

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

	@Override
	public Token getTokenInfo(String token) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("validate 시작(token=" + token + ")");
		Token tokenData = tokenMapper.selectTokenInfo(token);
		return tokenData;
	}

}
