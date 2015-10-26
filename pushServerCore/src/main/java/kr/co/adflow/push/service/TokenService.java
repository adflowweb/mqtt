/*
 * 
 */
package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface TokenService.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface TokenService {

	/**
	 * Gets the.
	 *
	 * @param token the token
	 * @return the token
	 * @throws Exception the exception
	 */
	Token get(String token) throws Exception;

	/**
	 * Post.
	 *
	 * @param user the user
	 * @return the token
	 * @throws Exception the exception
	 */
	Token post(User user) throws Exception;

	/**
	 * Put.
	 *
	 * @param token the token
	 * @return the int
	 * @throws Exception the exception
	 */
	int put(Token token) throws Exception;

	/**
	 * Delete.
	 *
	 * @param token the token
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String token) throws Exception;

	/**
	 * Validate.
	 *
	 * @param token the token
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	boolean validate(String token) throws Exception;
	
	
	
	//140901 - update:kicho -start
	/**
	 * Gets the by user.
	 *
	 * @param userID the user id
	 * @return the by user
	 * @throws Exception the exception
	 */
	Token[] getByUser(String userID) throws Exception;
	//140901 - update:kicho -end
	
	//140901 - update:kicho -start
	/**
	 * Gets the multi by user.
	 *
	 * @param userID the user id
	 * @return the multi by user
	 * @throws Exception the exception
	 */
	Token[] getMultiByUser(String userID) throws Exception;
	//140901 - update:kicho -end

	Token[] getMultiByUfmi(String ufmi) throws Exception;
}
