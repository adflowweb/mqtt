package kr.co.adflow.pms.adm.request;

import org.hibernate.validator.constraints.NotEmpty;

public class UserReq {
	
	@NotEmpty
	private String userId;
	@NotEmpty
	private String password;
	@NotEmpty
	private int msgCntLimit;
	@NotEmpty
	private String role;
	private String userName;
	private String ipFilters;
	
	private boolean isOptions = false;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMsgCntLimit() {
		return msgCntLimit;
	}
	public void setMsgCntLimit(int msgCntLimit) {
		this.msgCntLimit = msgCntLimit;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	public boolean isOptions() {
		return isOptions;
	}
	public void setOptions(boolean isOptions) {
		this.isOptions = isOptions;
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
