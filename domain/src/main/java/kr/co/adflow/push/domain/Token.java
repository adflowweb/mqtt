package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Token {

	private String tokenID;
	private String userID;
	private String deviceID;

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	@Override
	public String toString() {
		return "Token [tokenID=" + tokenID + ", userID=" + userID
				+ ", deviceID=" + deviceID + "]";
	}

}
