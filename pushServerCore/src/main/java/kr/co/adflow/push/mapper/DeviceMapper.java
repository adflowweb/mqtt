/*
 * 
 */
package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Device;

import org.apache.ibatis.annotations.Param;

// TODO: Auto-generated Javadoc
/**
 * The Interface DeviceMapper.
 */
public interface DeviceMapper {

	/**
	 * Gets the.
	 *
	 * @param userID the user id
	 * @param deviceID the device id
	 * @return the device
	 * @throws Exception the exception
	 */
	Device get(@Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	/**
	 * Increase unread.
	 *
	 * @param userID the user id
	 * @param deviceID the device id
	 * @return the int
	 * @throws Exception the exception
	 */
	int increaseUnread(@Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	/**
	 * Put unread.
	 *
	 * @param unRead the un read
	 * @param userID the user id
	 * @param deviceID the device id
	 * @return the int
	 * @throws Exception the exception
	 */
	int putUnread(@Param("unRead") int unRead, @Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	/**
	 * Gets the by user.
	 *
	 * @param userID the user id
	 * @return the by user
	 * @throws Exception the exception
	 */
	Device getByUser(String userID) throws Exception;

	/**
	 * Gets the all apple devices.
	 *
	 * @return the all apple devices
	 * @throws Exception the exception
	 */
	Device[] getAllAppleDevices() throws Exception;

	/**
	 * Post.
	 *
	 * @param device the device
	 * @return the int
	 * @throws Exception the exception
	 */
	int post(Device device) throws Exception;

	/**
	 * Put.
	 *
	 * @param device the device
	 * @return the int
	 * @throws Exception the exception
	 */
	int put(Device device) throws Exception;

	/**
	 * Delete.
	 *
	 * @param deviceID the device id
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String deviceID) throws Exception;

}
