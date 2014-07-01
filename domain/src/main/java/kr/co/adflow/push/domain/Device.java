package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 28.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Device {

	private String deviceID;
	private String userID;
	private String deviceInfo;
	private String apnsToken;

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getApnsToken() {
		return apnsToken;
	}

	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	@Override
	public String toString() {
		return "Device [deviceID=" + deviceID + ", userID=" + userID
				+ ", deviceInfo=" + deviceInfo + ", apnsToken=" + apnsToken
				+ "]";
	}

}
