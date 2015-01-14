/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.TokenDao;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.mapper.TokenMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenDaoImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Repository
public class TokenDaoImpl implements TokenDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenDaoImpl.class);

	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#get(java.lang.String)
	 */
	@Override
	public Token get(String token) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.get(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#post(java.lang.String)
	 */
	@Override
	public Token post(Token token) throws Exception {
		logger.debug("post시작(token=" + token + ")");
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		int result = tokenMapper.post(token);
		logger.debug("post종료(result=" + result + ")");
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#put(java.lang.String)
	 */
	@Override
	public int put(Token token) throws Exception {
		logger.debug("put시작(token=" + token + ")");
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		int result = tokenMapper.put(token);
		logger.debug("put종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.TokenDao#putLastAcessTime(kr.co.adflow.push.domain.Token)
	 */
	@Override
	public int putLastAcessTime(Token token) throws Exception {
		logger.debug("put시작(token=" + token + ")");
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		int result = tokenMapper.putLastAcessTime(token);
		logger.debug("put종료(result=" + result + ")");
		return result;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#delete(java.lang.String)
	 */
	@Override
	public int delete(String token) throws Exception {
		logger.debug("delete시작(token=" + token + ")");
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		int result = tokenMapper.delete(token);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.TokenDao#getLatest(kr.co.adflow.push.domain.Token)
	 */
	@Override
	public Token getLatest(Token token) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.getLatest(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#validate(java.lang.String)
	 */
	// @Override
	// public boolean validate(String token) throws Exception {
	// logger.debug("validate시작(token=" + token + ")");
	//
	// TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
	// // tokenMapper.validate(token);
	//
	// // Context initContext = new InitialContext();
	// // Context envContext = (Context) initContext.lookup("java:/comp/env");
	// // DataSource datasource = (DataSource)
	// // envContext.lookup("jdbc/TestDB");
	// // Connection con = datasource.getConnection();
	// // logger.debug("con=" + con);
	// // con.close();
	// logger.debug("validate종료(true)");
	// return true;
	// }
	

	//140901 <kicho> - start
	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#getByUser(java.lang.String)
	 */
	@Override
	public Token[] getByUser(String userID) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.getByUser(userID);
	}
	//140901 <kicho> - end
	
	//140902 <kicho> - start
	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#getMultiByUser(java.lang.String)
	 */
	@Override
	public Token[] getMultiByUser(String userID) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.getMultiByUser(userID);
	}
	//140902 <kicho> - end	
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.TokenDao#validateByUserID(java.lang.String)
	 */
	public boolean validateByUserID(String userID) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.validateByUserID(userID);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.TokenDao#validateByUfmi(java.lang.String)
	 */
	public boolean validateByUfmi(String ufmi) throws Exception {
		TokenMapper tokenMapper = sqlSession.getMapper(TokenMapper.class);
		return tokenMapper.validateByUfmi(ufmi);
	}
}
