package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.UserDAO;
import kr.co.adflow.push.domain.User;
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
	UserDAO userDao;

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
		int result = userDao.delete(userID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.UserService#auth(kr.co.adflow.push.domain.User)
	 */
	@Override
	public boolean auth(User user) throws Exception {
		logger.debug("auth시작(user=" + user + ")");
		boolean rst = userDao.auth(user);
		logger.debug("auth종료(result=" + rst + ")");
		return rst;
	}
}
