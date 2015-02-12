package kr.co.adflow.pms.domain;

public class MsgIdsParams {
	
	private String keyMon;
	private String issueId;
	private String[] msgIds;
	
	public String getKeyMon() {
		return keyMon;
	}
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public String[] getMsgIds() {
		return msgIds;
	}
	public void setMsgIds(String[] msgIds) {
		this.msgIds = msgIds;
	}

}
