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
	
	/** The role. */
	private String role;
	
	/** The password. */
	private String password;
	
	/** The user name. */
	private String userName;
	
	/** The ip filters. */
	private String ipFilters;
	
	/** The status. */
	private int status;
	
	/** The msg cnt limit. */
	private int msgCntLimit;
	
	/** The issue time. */
	private Date issueTime;
	
	/** The issue id. */
	private String issueId;
	
	/** The action. */
	private String action;
	
	/** The application token. */
	private String applicationToken;

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
	
	/** The ufmi. */
	private String ufmi;
	
	/** The saId. */
	private String saId;
	
	/** The groupTopict. */
	private String groupTopic;

	
	/**
	 * Gets the ufmi.
	 *
	 * @return the ufmi
	 */
	public String getUfmi() {
		return ufmi;
	}

	
	/**
	 * Sets the ufmi.
	 *
	 * @param ufmi the new ufmi
	 */
	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

	/**
	 * Gets the saId.
	 *
	 * @return the saId
	 */
	public String getSaId() {
		return saId;
	}

	/**
	 * Sets the saId.
	 *
	 * @param saId the new saId
	 */
	public void setSaId(String saId) {
		this.saId = saId;
	}

	/**
	 * Gets the groupTopic.
	 *
	 * @return the groupTopic
	 */
	public String getGroupTopic() {
		return groupTopic;
	}

	/**
	 * Sets the groupTopic.
	 *
	 * @param groupTopic the new groupTopic
	 */
	public void setGroupTopic(String groupTopic) {
		this.groupTopic = groupTopic;
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
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @param issueTime the new issue time
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
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
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
	 * @param issueId the new issue id
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
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Gets the application token.
	 *
	 * @return the application token
	 */
	public String getApplicationToken() {
		return applicationToken;
	}

	/**
	 * Sets the application token.
	 *
	 * @param applicationToken the new application token
	 */
	public void setApplicationToken(String applicationToken) {
		this.applicationToken = applicationToken;
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
