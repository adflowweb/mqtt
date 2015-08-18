/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountServiceImpl.
 */
@Service
public class AccountServiceImpl implements AccountService {

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/** The check util. */
	@Autowired
	private CheckUtil checkUtil;

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.AccountService#retrieveAccount(java.lang.String)
	 */
	@Override
	public User retrieveAccount(String appKey) throws Exception {
		User resultUser = null;
		String userId = interceptMapper.selectCashedUserId(appKey);
		resultUser = userMapper.select(userId);

		resultUser.setDefaultExpiry(checkUtil.getMessageExpiry(resultUser
				.getDefaultExpiry()));
		resultUser.setDefaultQos(checkUtil.getMessageQos(resultUser
				.getDefaultQos()));
		

		return resultUser;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.AccountService#modifyPassword(kr.co.adflow.pms.adm.request.PasswordReq, java.lang.String)
	 */
	@Override
	public int modifyPassword(PasswordReq req, String appKey)  throws Exception{

		if (!req.getNewPassword().trim().equals(req.getRePassword().trim())) {
//			throw new RuntimeException("패스워드 변경 실패1");
			throw new PmsRuntimeException("패스워드 변경 실패1");
		}

		// TODO OLD <> NEW 확인 필요?

		String userId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = new User();
		paramUser.setUserId(userId);
		paramUser.setPassword(this.getPassword(userId, req.getOldPassword()));

		User user = userMapper.selectAuth(paramUser);
		if (user == null) {
//			throw new RuntimeException("패스워드 변경 실패2");
			throw new PmsRuntimeException("user not valid");
		}

		paramUser.setAction("modifyPassword");
		paramUser.setPassword(this.getPassword(userId, req.getNewPassword()));
		paramUser.setIssueId(userId);

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updatePassword(paramUser);
		} else {
//			throw new RuntimeException("패스워드 변경 실패3");
			throw new PmsRuntimeException("패스워드 변경 실패3");
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

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.AccountService#modifyAccount(kr.co.adflow.pms.adm.request.AccountReq, java.lang.String)
	 */
	@Override
	public int modifyAccount(AccountReq req, String appKey)  throws Exception{

		String userId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = userMapper.select(userId);

		paramUser.setUserName(req.getUserName());
		if (req.getIpFilters().trim().length() != 0) {
			paramUser.setIpFilters(req.getIpFilters());
		}
		paramUser.setIssueId(userId);
		paramUser.setAction("modifyAccount");

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updateUser(paramUser);
		} else {
//			throw new RuntimeException("modifyAccount fail");
			throw new PmsRuntimeException("modifyAccount fail");
		}

		return cnt;
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.AccountService#modifyPassword(kr.co.adflow.pms.adm.request.PasswordReq, java.lang.String)
	 */
	@Override
	public int modifyUserName(UserReq req, String appKey) throws Exception{

		if (req.getUserName() == null ||req.getUserName().trim().length() == 0) {
//			throw new RuntimeException("유저 이름 변경 실패1");
			throw new PmsRuntimeException("User name not valid. userName::"+ req.getUserName());
		}

		// TODO OLD <> NEW 확인 필요?

		String userId = interceptMapper.selectCashedUserId(appKey);
		
		User paramUser = userMapper.select(userId);
		if (paramUser == null) {
//			throw new RuntimeException("패스워드 변경 실패2");
			throw new PmsRuntimeException("user not found");
		}

//		User paramUser = new User();
//		paramUser.setUserId(userId);
		paramUser.setUserName(req.getUserName());


		paramUser.setAction("modifyUserName");
		paramUser.setIssueId(userId);

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updateUserName(paramUser);
		} else {
//			throw new RuntimeException("유저 이름 변경 실패2");
			throw new PmsRuntimeException("user name Update fail");
		}

		return cnt;
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.AccountService#modifyPassword(kr.co.adflow.pms.adm.request.PasswordReq, java.lang.String)
	 */
	@Override
	public int modifySvcUser(UserReq req, String appKey) throws Exception{

		if (req.getUserName() == null ||req.getUserName().trim().length() == 0) {
			throw new PmsRuntimeException("User name not valid. userName::"+ req.getUserName());
		}
		
		if (req.getCallbackUrl() == null ||req.getCallbackUrl().trim().length() == 0) {
			throw new PmsRuntimeException("User callback url not valid. callbackUrl::"+ req.getCallbackUrl());
		}

		// TODO OLD <> NEW 확인 필요?

		String userId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = userMapper.select(userId);
		if (paramUser == null) {
			throw new PmsRuntimeException("user not found");
		}
		
		paramUser.setUserName(req.getUserName());
	

		paramUser.setAction("modifySvcUser");
		paramUser.setIssueId(userId);

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updateSvcUser(paramUser);
		} else {
			throw new PmsRuntimeException("svc user Update fail");
		}

		return cnt;
	}
}
