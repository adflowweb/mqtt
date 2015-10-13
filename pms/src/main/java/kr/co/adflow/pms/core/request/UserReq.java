/*
 * 
 */
package kr.co.adflow.pms.core.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class UserReq {

	/** The user id. */
	@NotEmpty
	private String userId;
	
	/** The password. */
	@NotEmpty
	private String password;
	
	/** The msg cnt limit. */
	@NotEmpty
	private int msgCntLimit;
	
	/** The role. */
	@NotEmpty
	private String role;
	
	/** The user name. */
	private String userName;
	
	/** The ip filters. */
	private String ipFilters;

	/** The is options. */
	private boolean isOptions = false;

	/** The default expiry. */
	private int defaultExpiry;
	
	/** The default qos. */
	private int defaultQos;
	
	/** The msg size limit. */
	private int msgSizeLimit;

	/** The callback url. */
	private String callbackUrl;
	
	/** The callback method. */
	private String callbackMethod;
	
	/** The callback cnt limit. */
	private int callbackCntLimit;

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
	 * @param userId the new user id
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
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the msg cnt limit.
	 *
	 * @return the msg cnt limit
	 */
	public int getMsgCntLimit() {
		return msgCntLimit;
	}

	/**
	 * Sets the msg cnt limit.
	 *
	 * @param msgCntLimit the new msg cnt limit
	 */
	public void setMsgCntLimit(int msgCntLimit) {
		this.msgCntLimit = msgCntLimit;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
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
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the ip filters.
	 *
	 * @return the ip filters
	 */
	public String getIpFilters() {
		return ipFilters;
	}

	/**
	 * Sets the ip filters.
	 *
	 * @param ipFilters the new ip filters
	 */
	public void setIpFilters(String ipFilters) {
		this.ipFilters = ipFilters;
	}

	/**
	 * Checks if is options.
	 *
	 * @return true, if is options
	 */
	public boolean isOptions() {
		return isOptions;
	}

	/**
	 * Sets the options.
	 *
	 * @param isOptions the new options
	 */
	public void setOptions(boolean isOptions) {
		this.isOptions = isOptions;
	}

	/**
	 * Gets the default expiry.
	 *
	 * @return the default expiry
	 */
	public int getDefaultExpiry() {
		return defaultExpiry;
	}

	/**
	 * Sets the default expiry.
	 *
	 * @param defaultExpiry the new default expiry
	 */
	public void setDefaultExpiry(int defaultExpiry) {
		this.defaultExpiry = defaultExpiry;
	}

	/**
	 * Gets the default qos.
	 *
	 * @return the default qos
	 */
	public int getDefaultQos() {
		return defaultQos;
	}

	/**
	 * Sets the default qos.
	 *
	 * @param defaultQos the new default qos
	 */
	public void setDefaultQos(int defaultQos) {
		this.defaultQos = defaultQos;
	}

	/**
	 * Gets the msg size limit.
	 *
	 * @return the msg size limit
	 */
	public int getMsgSizeLimit() {
		return msgSizeLimit;
	}

	/**
	 * Sets the msg size limit.
	 *
	 * @param msgSizeLimit the new msg size limit
	 */
	public void setMsgSizeLimit(int msgSizeLimit) {
		this.msgSizeLimit = msgSizeLimit;
	}

	/**
	 * Gets the callback url.
	 *
	 * @return the callback url
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}

	/**
	 * Sets the callback url.
	 *
	 * @param callbackUrl the new callback url
	 */
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	/**
	 * Gets the callback method.
	 *
	 * @return the callback method
	 */
	public String getCallbackMethod() {
		return callbackMethod;
	}

	/**
	 * Sets the callback method.
	 *
	 * @param callbackMethod the new callback method
	 */
	public void setCallbackMethod(String callbackMethod) {
		this.callbackMethod = callbackMethod;
	}

	/**
	 * Gets the callback cnt limit.
	 *
	 * @return the callback cnt limit
	 */
	public int getCallbackCntLimit() {
		return callbackCntLimit;
	}

	/**
	 * Sets the callback cnt limit.
	 *
	 * @param callbackCntLimit the new callback cnt limit
	 */
	public void setCallbackCntLimit(int callbackCntLimit) {
		this.callbackCntLimit = callbackCntLimit;
	}

}
