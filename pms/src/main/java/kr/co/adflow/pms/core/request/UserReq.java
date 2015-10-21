/*
 * 
 */
package kr.co.adflow.pms.core.request;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class UserReq {

	/** The user id. */

	private String userId;

	/** The password. */

	private String password;

	/** The msg cnt limit. */

	private int role;

	/** The user name. */
	private String userName;

	private String deviceId;
	private String deviceInfo;

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	@Override
	public String toString() {
		return "UserReq [userId=" + userId + ", password=" + password
				+ ", role=" + role + ", userName=" + userName + ", deviceId="
				+ deviceId + ", deviceInfo=" + deviceInfo + "]";
	}

}
