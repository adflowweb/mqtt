package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Token;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface TokenService {

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	Token get(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	void post(Token token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	void put(Token token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	void delete(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	boolean validate(String token) throws Exception;
}
