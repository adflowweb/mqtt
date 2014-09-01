package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;

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
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Token post(User user) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	int put(Token token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	int delete(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	boolean validate(String token) throws Exception;
	
	
	
	//140901 - update:kicho -start
	/**
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	Token[] getByUser(String userID) throws Exception;
	//140901 - update:kicho -end
}
