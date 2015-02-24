package kr.co.adflow.pms.domain;

import java.util.Date;

public class CtlQ {

	private String exeType;
	private String tableName;
	private String msgId;
	private String issueId;
	private Date   issueTime;
	private String serverId;
	
	public String getExeType() {
		return exeType;
	}
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public Date getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
}
