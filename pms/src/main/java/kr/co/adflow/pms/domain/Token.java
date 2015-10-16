/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Token.
 */
public class Token {

	/** The user id. */
	private String userId;

	/** The token id. */
	private String tokenId;

	private String deviceId;

	/** The token type. */
	private String tokenType;

	/** The issue time. */
	private Date issueTime;

	/** The issue id. */
	private String issueId;

	/** The expired time. */
	private Date expiredTime;

	private String apiCode;

	private String serverId;

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */

	public String getUserId() {
		return userId;
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

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	@Override
	public String toString() {
		return "Token [userId=" + userId + ", tokenId=" + tokenId
				+ ", tokenType=" + tokenType + ", issueTime=" + issueTime
				+ ", issueId=" + issueId + ", expiredTime=" + expiredTime + "]";
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
	 * Gets the token id.
	 * 
	 * @return the token id
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * Sets the token id.
	 * 
	 * @param tokenId
	 *            the new token id
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * Gets the token type.
	 * 
	 * @return the token type
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * Sets the token type.
	 * 
	 * @param tokenType
	 *            the new token type
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
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
	 * Gets the expired time.
	 * 
	 * @return the expired time
	 */
	public Date getExpiredTime() {
		return expiredTime;
	}

	/**
	 * Sets the expired time.
	 * 
	 * @param expiredTime
	 *            the new expired time
	 */
	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
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

}
