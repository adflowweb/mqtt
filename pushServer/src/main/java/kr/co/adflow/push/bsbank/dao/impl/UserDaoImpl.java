package kr.co.adflow.push.bsbank.dao.impl;

import kr.co.adflow.push.bsbank.dao.UserDao;
import kr.co.adflow.push.bsbank.mapper.UserMapper;
import kr.co.adflow.push.domain.bsbank.User;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Repository("bsBankUserDaoImpl")
public class UserDaoImpl implements UserDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	@Autowired
	@Qualifier("bsBanksqlSession")
	private SqlSession sqlSession;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDAO#get(java.lang.String)
	 */
	@Override
	public User get(String userID) throws Exception {
		logger.debug("get시작(userID=" + userID + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.get(userID);
		logger.debug("get종료(user=" + user + ")");
		return user;
	}

	@Override
	public User[] getUsersByDepartment(String dept) {
		logger.debug("getUsersByDepartment시작(dept=" + dept + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User[] user = userMapper.getUsersByDepartment(dept);
		logger.debug("getUsersByDepartment종료(user=" + user + ")");
		return user;
	}
}
