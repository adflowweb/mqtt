/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserDao.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface UserDao {
	
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

	//KTP-skip-start
//	int putWithoutRole(User user) throws Exception;
	//KTP-skip-end

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
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	boolean auth(User user) throws Exception;

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
