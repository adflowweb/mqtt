package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.UserDAO;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.mapper.UserMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Repository
public class UserDAOImpl implements UserDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserDAOImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	@Autowired
	private SqlSession sqlSession;

	@Override
	public User get(String userID) throws Exception {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		return userMapper.get(userID);
	}

	@Override
	public void post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		userMapper.post(user);
		logger.debug("post종료()");
	}

	@Override
	public void put(User user) throws Exception {
	}

	@Override
	public void delete(String userID) throws Exception {
		logger.debug("delete시작(userID=" + userID + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		userMapper.delete(userID);
		logger.debug("delete종료()");
	}

}
