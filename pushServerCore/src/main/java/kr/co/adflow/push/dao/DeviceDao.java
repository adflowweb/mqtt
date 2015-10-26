/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Device;

// TODO: Auto-generated Javadoc
/**
 * The Interface DeviceDao.
 *
 * @author nadir93
 * @date 2014. 4. 28.
 */
public interface DeviceDao {
	
	/**
	 * Gets the.
	 *
	 * @param deviceID the device id
	 * @return the device
	 * @throws Exception the exception
	 */
	Device get(String deviceID) throws Exception;

	/**
	 * Gets the by user.
	 *
	 * @param userID the user id
	 * @return the by user
	 * @throws Exception the exception
	 */
	Device getByUser(String userID) throws Exception;

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
