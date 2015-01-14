/*
 * 
 */
package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserService.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface UserService {
	
	/**
	 * Gets the.
	 *
	 * @param userID the user id
	 * @return the user
	 * @throws Exception the exception
	 */
	User get(String userID) throws Exception;

	/**
	 * Post.
	 *
	 * @param user the user
	 * @return the int
	 * @throws Exception the exception
	 */
	int post(User user) throws Exception;

	/**
	 * Put.
	 *
	 * @param user the user
	 * @return the int
	 * @throws Exception the exception
	 */
	int put(User user) throws Exception;

	/**
	 * Delete.
	 *
	 * @param userID the user id
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String userID) throws Exception;

	/**
	 * Auth.
	 *
	 * @param user the user
	 * @return the token
	 * @throws Exception the exception
	 */
	Token auth(User user) throws Exception;

	/**
	 * Admin auth.
	 *
	 * @param user the user
	 * @return the token
	 * @throws Exception the exception
	 */
	Token adminAuth(User user) throws Exception;

	/**
	 * Gets the admin.
	 *
	 * @return the admin
	 * @throws Exception the exception
	 */
	User[] getAdmin() throws Exception;
	
	/**
	 * Change password.
	 *
	 * @param user the user
	 * @return the int
	 * @throws Exception the exception
	 */
	int changePassword(User user) throws Exception;
}
