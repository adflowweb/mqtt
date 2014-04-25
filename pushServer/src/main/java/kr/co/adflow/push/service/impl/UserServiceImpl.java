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
	public void post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		userDao.post(user);
		logger.debug("post종료()");
	}

	@Override
	public void put(User user) throws Exception {
		logger.debug("put시작(user=" + user + ")");
		userDao.put(user);
		logger.debug("put종료()");
	}

	@Override
	public void delete(String userID) throws Exception {
		logger.debug("delete시작(userID=" + userID + ")");
		userDao.delete(userID);
		logger.debug("delete종료()");
	}

}
