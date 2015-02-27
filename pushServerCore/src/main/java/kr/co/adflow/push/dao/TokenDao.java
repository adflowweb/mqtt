/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Token;

// TODO: Auto-generated Javadoc
/**
 * The Interface TokenDao.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface TokenDao {

	/**
	 * Gets the.
	 *
	 * @param token the token
	 * @return the token
	 * @throws Exception the exception
	 */
	Token get(String token) throws Exception;

	/**
	 * Gets the latest.
	 *
	 * @param token the token
	 * @return the latest
	 * @throws Exception the exception
	 */
	Token getLatest(Token token) throws Exception;

	/**
	 * Post.
	 *
	 * @param token the token
	 * @return the token
	 * @throws Exception the exception
	 */
	Token post(Token token) throws Exception;

	/**
	 * Put.
	 *
	 * @param token the token
	 * @return the int
	 * @throws Exception the exception
	 */
	int put(Token token) throws Exception;
	
	/**
	 * Put last acess time.
	 *
	 * @param token the token
	 * @return the int
	 * @throws Exception the exception
	 */
	int putLastAcessTime(Token token) throws Exception;

	/**
	 * Delete.
	 *
	 * @param token the token
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String token) throws Exception;

	// boolean validate(String token) throws Exception;
	
	/**
	 * Gets the by user.
	 *
	 * @param userID the user id
	 * @return the by user
	 * @throws Exception the exception
	 */
	Token[] getByUser(String userID) throws Exception;
	
	/**
	 * Gets the multi by user.
	 *
	 * @param userID the user id
	 * @return the multi by user
	 * @throws Exception the exception
	 */
	Token[] getMultiByUser(String userID) throws Exception;
	
	
	/**
	 * Validate by user id.
	 *
	 * @param userID the user id
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	boolean validateByUserID(String userID) throws Exception;
	
	/**
	 * Validate by ufmi.
	 *
	 * @param ufmi the ufmi
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	boolean validateByUfmi(String ufmi) throws Exception;
	
	Token[] expiredSessionList(int lastAccessTime) throws Exception;

}
