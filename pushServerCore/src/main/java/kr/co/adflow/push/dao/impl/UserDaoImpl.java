/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.UserDao;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.mapper.UserMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDaoImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Repository
public class UserDaoImpl implements UserDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	// @Resource
	// private LdapAuthDao ldap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDAO#get(java.lang.String)
	 */
	@Override
	public User get(String userID) throws Exception {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		return userMapper.get(userID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDAO#post(kr.co.adflow.push.domain.User)
	 */
	@Override
	public int post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		int result = userMapper.post(user);
		logger.debug("post종료(result=" + result + ")");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDAO#put(kr.co.adflow.push.domain.User)
	 */
	@Override
	public int put(User user) throws Exception {
		logger.debug("put시작(user=" + user + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		int result = userMapper.put(user);
		logger.debug("put종료(result=" + result + ")");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDAO#delete(java.lang.String)
	 */
	@Override
	public int delete(String userID) throws Exception {
		logger.debug("delete시작(userID=" + userID + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		int result = userMapper.delete(userID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/*
	 * LDAP 인증
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDao#auth(kr.co.adflow.push.domain.User)
	 */
	@Override
	public boolean auth(User user) throws Exception {
		logger.debug("auth시작(user=" + user + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		boolean rst = userMapper.auth(user);

		// ldap 인증
		// boolean rst = ldap.auth(user.getUserID(), user.getPassword());
		// logger.debug("auth종료(result=" + rst + ")");
		return rst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.UserDao#getAdmin()
	 */
	@Override
	public User[] getAdmin() throws Exception {
		logger.debug("getAdmin시작()");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User[] users = userMapper.getAdmin();
		logger.debug("getAdmin종료(user=" + users + ")");
		return users;
	}

	// KTP-skip-start
	// @Override
	// public int putWithoutRole(User user) throws Exception {
	// logger.debug("putWithoutRole시작(user=" + user + ")");
	// UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	// int result = userMapper.putWithoutRole(user);
	// logger.debug("putWithoutRole종료(result=" + result + ")");
	// return result;
	// }
	// KTP-skip-end

	// 140901 <kicho> - start
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.UserDAO#changePassword(kr.co.adflow.push.domain.
	 * User)
	 */
	@Override
	public int changePassword(User user) throws Exception {
		logger.debug("changePassword시작(user=" + user + ")");
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		int result = userMapper.changePassword(user);
		logger.debug("changePassword종료(result=" + result + ")");
		return result;
	}
	// 140901 <kicho> - end

}
