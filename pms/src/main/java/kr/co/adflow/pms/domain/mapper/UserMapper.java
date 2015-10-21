/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.UserInfo;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserMapper.
 */
public interface UserMapper {

	/**
	 * Insert user.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int insertUser(User user);

	/**
	 * Select.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user
	 */
	List<UserInfo> selectUserInfo(String userId);

	User selectUpdateUser(String userId);

	List<User> selectDeleteUser(String userId);

	/**
	 * Update user.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updateUser(User user);

	/**
	 * Update user status.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updateUserStatus(User user);

	/**
	 * Delete user.
	 * 
	 * @param userId
	 *            the user id
	 * @return the int
	 */
	int deleteUser(String userId);

	/**
	 * Select auth.
	 * 
	 * @param user
	 *            the user
	 * @return the user
	 */
	User selectAuth(User user);

	/**
	 * Select user.
	 * 
	 * @param userid
	 *            the user id
	 * @return the user id
	 */
	String selectUser(String userId);

	String selectUserid(String userId);

	/**
	 * List all.
	 * 
	 * @return the list
	 */
	List<User> listAll();

	/**
	 * Log user history.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int logUserHistory(User user);

	/**
	 * Gets the msg cnt limit.
	 * 
	 * @param userId
	 *            the user id
	 * @return the msg cnt limit
	 */
	int getMsgCntLimit(String userId);

	/**
	 * Update msg cnt limit.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updateMsgCntLimit(User user);

	/**
	 * Discount msg cnt limit.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int discountMsgCntLimit(User user);

	/**
	 * Update password.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updatePassword(User user);

	/**
	 * Gets the User Ufmi.
	 * 
	 * @param userId
	 *            the user id
	 * @return the Ufmi
	 */
	String selectUfmi(String userId);

	/**
	 * Gets the User Role.
	 * 
	 * @param userId
	 *            the user id
	 * @return the Role.
	 */
	String selectRole(String userId);

	/**
	 * Update user name.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updateUserName(User user);

	/**
	 * Update svc user.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	int updateSvcUser(User user);

	/**
	 * Gets the callback url.
	 * 
	 * @param tokenId
	 *            the token id
	 * @return the String.
	 */
	String selectCallbackUrl(Map<String, Object> params);

}
