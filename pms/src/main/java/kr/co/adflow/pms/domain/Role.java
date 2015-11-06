package kr.co.adflow.pms.domain;

import java.util.Date;

public class Role {

	private int groupCode;
	private int apiCode;
	private String apiUrl;
	private String apiMethod;
	private String apiName;
	private Date issueTime;
	private String issueId;

	public int getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}

	public int getApiCode() {
		return apiCode;
	}

	public void setApiCode(int apiCode) {
		this.apiCode = apiCode;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiMethod() {
		return apiMethod;
	}

	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	@Override
	public String toString() {
		return "Role [groupCode=" + groupCode + ", apiCode=" + apiCode
				+ ", apiUrl=" + apiUrl + ", apiMethod=" + apiMethod
				+ ", apiName=" + apiName + ", issueTime=" + issueTime
				+ ", issueId=" + issueId + "]";
	}

}
