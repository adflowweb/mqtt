/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Device.
 *
 * @author nadir93
 * @date 2014. 4. 28.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Device {

	/** The device id. */
	private String deviceID;
	
	/** The user id. */
	private String userID;
	
	/** The device info. */
	private String deviceInfo;
	
	/** The apns token. */
	private String apnsToken;
	
	/** The un read. */
	private int unRead;

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceID() {
		return deviceID;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceID the new device id
	 */
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userID the new user id
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Gets the device info.
	 *
	 * @return the device info
	 */
	public String getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets the device info.
	 *
	 * @param deviceInfo the new device info
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * Gets the apns token.
	 *
	 * @return the apns token
	 */
	public String getApnsToken() {
		return apnsToken;
	}

	/**
	 * Sets the apns token.
	 *
	 * @param apnsToken the new apns token
	 */
	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	/**
	 * Gets the un read.
	 *
	 * @return the un read
	 */
	public int getUnRead() {
		return unRead;
	}

	/**
	 * Sets the un read.
	 *
	 * @param unRead the new un read
	 */
	public void setUnRead(int unRead) {
		this.unRead = unRead;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [deviceID=" + deviceID + ", userID=" + userID
				+ ", deviceInfo=" + deviceInfo + ", apnsToken=" + apnsToken
				+ ", unRead=" + unRead + "]";
	}

}
