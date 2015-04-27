/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface SystemService.
 */
public interface SystemService {

	/**
	 * List all user.
	 *
	 * @return the list
	 */
	public List<User> listAllUser();

	/**
	 * Creates the user.
	 *
	 * @param userReq the user req
	 * @param appKey the app key
	 * @return the string
	 */
	public String createUser(UserReq userReq, String appKey);

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
	 * @param userReq the user req
	 * @param appKey the app key
	 * @return the int
	 */
	public int updateUser(UserUpdateReq userReq, String appKey);

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @param appKey the app key
	 * @return the int
	 */
	public int deleteUser(String userId, String appKey);

	/**
	 * Gets the sys message list.
	 *
	 * @param params the params
	 * @return the sys message list
	 */
	public MessagesRes getSysMessageList(Map<String, String> params);

	/**
	 * Gets the sys resevation message list.
	 *
	 * @param params the params
	 * @return the sys resevation message list
	 */
	public MessagesRes getSysResevationMessageList(Map<String, String> params);

	/**
	 * Gets the server info.
	 *
	 * @return the server info
	 */
	public ServerInfo getServerInfo();

	/**
	 * Cancel reservation list.
	 *
	 * @param appKey the app key
	 * @param ids the ids
	 * @return the int
	 */
	public int cancelReservationList(String appKey, ReservationCancelReq ids);

	/**
	 * Gets the month summary.
	 *
	 * @param appKey the app key
	 * @param keyMon the key mon
	 * @param issueId the issue id
	 * @return the month summary
	 */
	public List<Map<String, Object>> getMonthSummary(Map<String, String> params, 
			String appKey,
			String keyMon, 
			String issueId);
	
	/**
	 * Test.
	 *
	 */
	public String testRun();

}
