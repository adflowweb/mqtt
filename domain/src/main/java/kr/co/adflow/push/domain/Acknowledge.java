package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 6. 13.
 */
public class Acknowledge {

	private int id;
	private String userID;
	private String deviceID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "Acknowledge [id=" + id + ", userID=" + userID + ", deviceID="
				+ deviceID + "]";
	}

}
