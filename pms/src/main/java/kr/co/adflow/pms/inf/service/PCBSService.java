/*
 * 
 */
package kr.co.adflow.pms.inf.service;

import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;

// TODO: Auto-generated Javadoc
/**
 * The Interface PCBSService.
 */
public interface PCBSService {

	/**
	 * Adds the user.
	 *
	 * @param userReq the user req
	 * @param appKey the app key
	 * @return the string
	 */
	public String addUser(UserReq userReq, String issueId);

	/**
	 * Retrieve user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	public User retrieveUser(String userId);

	/**
	 * Update user.
	 *
	 * @param user the user
	 * @param appKey the app key
	 * @return the int
	 */
	public int updateUser(UserUpdateReq user, String appKey);

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @param appKey the app key
	 * @return the int
	 */
	public int deleteUser(String userId, String appKey);

	/**
	 * Modify password.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the int
	 */
	public int modifyPassword(PasswordReq req, String appKey);

}
