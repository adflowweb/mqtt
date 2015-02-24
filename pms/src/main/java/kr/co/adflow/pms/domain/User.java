package kr.co.adflow.pms.domain;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

	private String userId;
	private String role;
	private String password;
	private String userName;
	private String ipFilters;
	private int status;
	private int msgCntLimit;
	private Date issueTime;
	private String issueId;
	private String action;
	private String applicationToken;
	
	private int defaultExpiry;
	private int defaultQos;
	private int msgSizeLimit;
	
	private String callbackUrl;
	private String callbackMethod;
	private int callbackCntLimit;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpFilters() {
		return ipFilters;
	}

	public void setIpFilters(String ipFilters) {
		this.ipFilters = ipFilters;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMsgCntLimit() {
		return msgCntLimit;
	}

	public void setMsgCntLimit(int msgCntLimit) {
		this.msgCntLimit = msgCntLimit;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getApplicationToken() {
		return applicationToken;
	}

	public void setApplicationToken(String applicationToken) {
		this.applicationToken = applicationToken;
	}

	public int getDefaultExpiry() {
		return defaultExpiry;
	}

	public void setDefaultExpiry(int defaultExpiry) {
		this.defaultExpiry = defaultExpiry;
	}

	public int getDefaultQos() {
		return defaultQos;
	}

	public void setDefaultQos(int defaultQos) {
		this.defaultQos = defaultQos;
	}

	public int getMsgSizeLimit() {
		return msgSizeLimit;
	}

	public void setMsgSizeLimit(int msgSizeLimit) {
		this.msgSizeLimit = msgSizeLimit;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCallbackMethod() {
		return callbackMethod;
	}

	public void setCallbackMethod(String callbackMethod) {
		this.callbackMethod = callbackMethod;
	}

	public int getCallbackCntLimit() {
		return callbackCntLimit;
	}

	public void setCallbackCntLimit(int callbackCntLimit) {
		this.callbackCntLimit = callbackCntLimit;
	}

}
