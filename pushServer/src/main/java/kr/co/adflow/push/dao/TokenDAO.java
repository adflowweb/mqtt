package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Token;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface TokenDAO {

	Token get(String token) throws Exception;

	void post(Token token) throws Exception;

	void put(Token token) throws Exception;

	void delete(String token) throws Exception;

	// boolean validate(String token) throws Exception;

}
