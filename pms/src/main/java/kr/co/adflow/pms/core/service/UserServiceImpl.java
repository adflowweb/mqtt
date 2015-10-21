package kr.co.adflow.pms.core.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.exception.UserRunTimeException;
import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.request.UserUpdateReq;
import kr.co.adflow.pms.core.response.TokenRes;
import kr.co.adflow.pms.core.response.UserInfoRes;
import kr.co.adflow.pms.core.response.UserRes;
import kr.co.adflow.pms.core.response.UserUpdateRes;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Device;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.UserInfo;
import kr.co.adflow.pms.domain.mapper.DeviceMapper;
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
			.getLogger(UserServiceImpl.class);

	@Autowired
	private CheckUtil checkUtil;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private InterceptMapper interceptMapper;

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private DeviceMapper deviceMapper;

	@Override
	public UserRes createUser(UserReq userReq, String requestId)
			throws Exception {

		User paramUser = new User();
		paramUser.setUserId(userReq.getUserId());
		paramUser.setUserName(userReq.getUserName());
		paramUser.setPassword(this.getPassword(userReq));
		paramUser.setIssueId(requestId);
		paramUser.setStatus(StaticConfig.USER_STATUS_NORMAL);
		paramUser.setAction("Create User");
		switch (userReq.getRole()) {
		// 관리자
		case 1:
			paramUser.setGroupCode("90000");
			break;
		// 사용자
		case 2:
			paramUser.setGroupCode("10000");
			break;
		}

		userMapper.logUserHistory(paramUser);

		try {
			userMapper.insertUser(paramUser);
		} catch (DuplicateKeyException e) {
			logger.debug(paramUser.getUserId() + "는 이미 등록된 유저 입니다.");
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_550500,
					"이미 등록된 유저입니다");

		}

		if (userReq.getDeviceId() == null
				|| userReq.getDeviceId().trim().length() == 0) {
			userReq.setDeviceId(this.getDeviceId());
			userReq.setDeviceInfo("서버생성 디바이스");
		}
		Token paramToken = new Token();
		paramToken.setUserId(userReq.getUserId());
		paramToken.setDeviceId(userReq.getDeviceId());
		paramToken.setIssueId(requestId);
		UserRes userRes = null;
		userRes = new UserRes();

		logger.debug("디바이스 등록 시작");
		Device device = new Device();

		device.setDeviceId(userReq.getDeviceId());
		device.setDeviceInfo(userReq.getDeviceInfo());
		device.setUserId(userReq.getUserId());

		int deviceResult = deviceMapper.insertDevice(device);

		if (deviceResult < 1) {
			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_550500,
					"디바이스 등록에 실패 하였습니다");
		}

		logger.debug("디바이스 등록 끝");
		logger.debug("새로운 토큰 발급");
		paramToken.setTokenId(KeyGenerator.generateToken(userReq.getUserId()));
		int tokenResult = tokenMapper.insertToken(paramToken);
		userRes.setToken(paramToken.getTokenId());
		userRes.setUserId(paramToken.getUserId());
		if (tokenResult < 1) {

			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_550500,
					"토큰 생성에 실패 하였습니다");
		}

		logger.debug("새로운 토큰 발급 끝");

		return userRes;
	}

	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	private String getDeviceId() {
		return KeyGenerator.generateDeviceId();
	}

	@Override
	public void updateUser(UserReq userReq, String requestUserId)
			throws Exception {

		String selectUserId = userMapper.selectUserid(userReq.getUserId());
		logger.debug(selectUserId);
		if (selectUserId == null || selectUserId.trim().length() == 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_552404,
					"사용자 아이디를 찾을 수 없습니다");
		}
		User userParam = userMapper.selectUpdateUser(selectUserId);

		logger.debug("사용자아이디:" + selectUserId);
		if (userReq.getUserName() != null)
			userParam.setUserName(userReq.getUserName());
		userParam.setStatus(StaticConfig.USER_STATUS_NORMAL);
		userParam.setIssueId(requestUserId);

		userParam.setAction("updateUser");
		userMapper.logUserHistory(userParam);
		int updateRst = userMapper.updateUser(userParam);

		if (updateRst < 1) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_552500,
					"사용자 정보수정에 실패하였습니다");
		}

	}

	@Override
	public UserInfoRes getUserInfo(String userId) throws Exception {
		UserInfoRes userInfoRes = new UserInfoRes();

		List<UserInfo> userInfo = userMapper.selectUserInfo(userId);
		if (userInfo == null || userInfo.size() == 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_551404,
					"해당 유저의 정보가 없습니다");
		}
		userInfoRes.setUserInfo(userInfo);
		return userInfoRes;
	}

	@Override
	public void deleteUser(String userId, String requsetUserId)
			throws Exception {
		String selectUserId = userMapper.selectUserid(userId);
		logger.debug(selectUserId);
		List<User> userParam = null;
		if (selectUserId == null || selectUserId.trim().length() == 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553404,
					"사용자 아이디를 찾을 수 없습니다");
		}
		try {
			userParam = userMapper.selectDeleteUser(selectUserId);
		} catch (Exception e) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553500,
					"삭제할 사용자를 찾을 수 없습니다");
		}

		try {
			userParam.get(0).setAction("deleteUser");
			userParam.get(0).setIssueId(requsetUserId);
			userParam.get(0).setStatus(StaticConfig.USER_STATUS_NORMAL);
			userMapper.logUserHistory(userParam.get(0));

		} catch (Exception e) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553500,
					"히스토리 업데이트에 실패하였습니다");
		}
		try {

			tokenMapper.deleteUserToken(userId);
		} catch (Exception e) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553500,
					"토큰 삭제에 실패하였습니다");
		}
		try {
			for (int i = 0; i < userParam.size(); i++) {
				deviceMapper.deleteDevice(userParam.get(i).getDeviceId());
			}

		} catch (Exception e) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553500,
					"디바이스 삭제에 실패하였습니다");
		}
		try {
			userMapper.deleteUser(userId);
		} catch (Exception e) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_553500,
					"사용자 삭제에 실패하였습니다");
		}

	}
}
