/*
 * 
 */
package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Device;

// TODO: Auto-generated Javadoc
/**
 * The Interface DeviceService.
 */
public interface DeviceService {
	
	/**
	 * Gets the.
	 *
	 * @param deviceID the device id
	 * @return the device
	 * @throws Exception the exception
	 */
	Device get(String deviceID) throws Exception;

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
