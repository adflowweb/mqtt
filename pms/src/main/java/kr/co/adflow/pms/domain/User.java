/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

	/** The user id. */
	private String userId;

	/** The password. */
	private String password;

	/** The user name. */
	private String userName;

	private String deviceId;
	private String token;

	/** The status. */
	private int status;

	/** The issue time. */
	private Date issueTime;

	/** The issue id. */
	private String issueId;

	/** The action. */
	private String action;

	private String serverId;

	private String groupCode;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeivceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

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
	 * /** Gets the password.
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

	/**
	 * Gets the issue time.
	 * 
	 * @return the issue time
	 */
	public Date getIssueTime() {
		return issueTime;
	}

	/**
	 * Sets the issue time.
	 * 
	 * @param issueTime
	 *            the new issue time
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the issue id.
	 * 
	 * @return the issue id
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * Sets the issue id.
	 * 
	 * @param issueId
	 *            the new issue id
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	/**
	 * Gets the action.
	 * 
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password
				+ ", userName=" + userName + ", deviceId=" + deviceId
				+ ", token=" + token + ", status=" + status + ", issueTime="
				+ issueTime + ", issueId=" + issueId + ", action=" + action
				+ ", serverId=" + serverId + ", groupCode=" + groupCode + "]";
	}

	/**
	 * Gets the msg size limit.
	 * 
	 * @return the msg size limit
	 */

}
