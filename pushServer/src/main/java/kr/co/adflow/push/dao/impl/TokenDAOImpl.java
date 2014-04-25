package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.TokenDAO;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.mapper.TokenMapper;

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
public class TokenDAOImpl implements TokenDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenDAOImpl.class);

	// Autowired를 사용하여 sqlSession을 사용할수 있다.
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
	public void post(Token token) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#put(java.lang.String)
	 */
	@Override
	public void put(Token token) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String token) throws Exception {
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
}
