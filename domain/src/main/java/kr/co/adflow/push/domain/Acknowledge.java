/*
 * 
 */
package kr.co.adflow.push.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class Acknowledge.
 *
 * @author nadir93
 * @date 2014. 6. 13.
 */
public class Acknowledge {

	/** The id. */
	private int id;
	
	/** The user id. */
	private String userID;
	
	/** The device id. */
	private String deviceID;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Acknowledge [id=" + id + ", userID=" + userID + ", deviceID="
				+ deviceID + "]";
	}

}
