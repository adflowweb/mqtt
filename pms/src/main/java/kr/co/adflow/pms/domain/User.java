package kr.co.adflow.pms.domain;

import java.util.Date;

public class User {

	private String userId;
	private String role;
	private String password;
	private String userName;
	private String ipFilters;
	private int status;
	private int msgCntLimitDay;
	private Date issueTime;
	private Date updateTime;

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

	public int getMsgCntLimitDay() {
		return msgCntLimitDay;
	}

	public void setMsgCntLimitDay(int msgCntLimitDay) {
		this.msgCntLimitDay = msgCntLimitDay;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
