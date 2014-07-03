package kr.co.adflow.push.bsbank.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.dao.UserDao;
import kr.co.adflow.push.bsbank.service.UserService;
import kr.co.adflow.push.domain.bsbank.User;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service("bsBankUserServiceImpl")
public class UserServiceImpl implements UserService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Resource
	UserDao userDao;

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
	public User[] getUsersByDepartment(String dept) throws Exception {
		logger.debug("getUsersByDepartment시작(dept=" + dept + ")");
		User[] users = userDao.getUsersByDepartment(dept);
		logger.debug("getUsersByDepartment종료(users=" + users + ")");
		return users;
	}

	@Override
	public User[] getUsersByName(String name) throws Exception {
		logger.debug("getUsersByName시작(name=" + name + ")");
		User[] users = userDao.getUsersByName(name);
		logger.debug("getUsersByName종료(users=" + users + ")");
		return users;
	}
}
