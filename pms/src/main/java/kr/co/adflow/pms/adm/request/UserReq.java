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


}
