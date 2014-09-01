package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.UserDao;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.TokenService;
import kr.co.adflow.push.service.UserService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class UserServiceImpl implements UserService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Resource
	private UserDao userDao;

	@Resource
	private TokenService tokenService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.UserService#get(java.lang.String)
	 */
	@Override
	public User get(String userID) throws Exception {
		logger.debug("get시작(userID=" + userID + ")");
		User user = userDao.get(userID);
		logger.debug("get종료(user=" + user + ")");
		return user;
	}

	@Override
	public int post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		int result = userDao.post(user);
		logger.debug("post종료(result=" + result + ")");
		return result;
	}

	@Override
	public int put(User user) throws Exception {
		logger.debug("put시작(user=" + user + ")");
		int result = userDao.put(user);
		logger.debug("put종료(result=" + result + ")");
		return result;
	}

	@Override
	public int delete(String userID) throws Exception {
		logger.debug("delete시작(userID=" + userID + ")");
		
		//140901 <kicho> -start
		//해당 토큰을 읽어와 삭제.
		Token[] tokens = tokenService.getByUser(userID);
		int resultToken;
		for (int i = 0; i < tokens.length; i++) {
			resultToken = tokenService.delete(tokens[i].getTokenID());
			logger.debug(" token["+tokens[i].getTokenID()+"] delete result=" + resultToken + ")");
		}
		//140901 <kicho> -end
		
		
		int result = userDao.delete(userID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/*
	 * 푸시사용자 로그인
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.UserService#auth(kr.co.adflow.push.domain.User)
	 */
	@Override
	public Token auth(User user) throws Exception {
		logger.debug("auth시작(user=" + user + ")");

		// KTP - Ldap 인증 무
		//boolean rst = userDao.auth(user);
		boolean rst = true;
		
		Token token = null;
		if (rst) {
			// token 발급
			token = tokenService.post(user);
		}
		logger.debug("auth종료(token=" + token + ")");
//		logger.debug("=============   Issue =" + token.getIssue().toString() + ")");
		return token;
	}

	 /*
	 * 어드민 로그인
	 *
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 kr.co.adflow.push.service.UserService#auth(kr.co.adflow.push.domain.User)
	 */
	 @Override
	 public Token adminAuth(User user) throws Exception {
	 logger.debug("auth시작(user=" + user + ")");
	
	 // DB 인증
	 boolean rst = userDao.auth(user);
	 
	 Token token = null;
	 if (rst) {
	 // token 발급
	 token = tokenService.post(user);
	 }
	 
//	 User adminUser = userDao.get(user.getUserID());
//	 if (!adminUser.isAdmin()) {
//	 token = null;
//	 }
	
	 logger.debug("auth종료(token=" + token + ")");
	 return token;
	 }

	@Override
	public User[] getAdmin() throws Exception {
		logger.debug("getAdmin시작()");
		User[] user = userDao.getAdmin();
		logger.debug("getAdmin종료(user=" + user + ")");
		return user;
	}
	
	
	//140901 <kicho> - start 
	//어드민 암호 변
	@Override
	public int changePassword(User user, String changePW) throws Exception {
		logger.debug("put시작(user=" + user + ")");
		
		boolean resultAuth = userDao.auth(user);
		
		int result=0;
		if (resultAuth) {
			user.setPassword(changePW);
			result = userDao.changePassword(user);
		}

		logger.debug("put종료(result=" + result + ")");
		return result;
	}
	//140901 <kicho> - end
	
}
