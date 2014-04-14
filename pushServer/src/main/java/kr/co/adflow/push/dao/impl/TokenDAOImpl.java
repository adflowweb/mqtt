package kr.co.adflow.push.dao.impl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.adflow.push.dao.TokenDAO;
import kr.co.adflow.push.domain.ResponseData;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component
public class TokenDAOImpl implements TokenDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#get(java.lang.String)
	 */
	@Override
	public ResponseData get(String token) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#post(java.lang.String)
	 */
	@Override
	public ResponseData post(String token) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#put(java.lang.String)
	 */
	@Override
	public ResponseData put(String token) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#delete(java.lang.String)
	 */
	@Override
	public ResponseData delete(String token) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.TokenDAO#validate(java.lang.String)
	 */
	@Override
	public boolean validate(String token) throws Exception {
		logger.debug("validate시작(token=" + token + ")");
		logger.debug("validate종료(true)");
		return true;
	}
}
