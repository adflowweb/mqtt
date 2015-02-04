package kr.co.adflow.pms.domain;

import java.util.Date;

public class Ack {

	private String keyMon;
	private String msgId;
	private String ackType;
	private String tokenId;
	private Date ackTime;
	private Date issueTime;
	private String issueId;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getAckType() {
		return ackType;
	}

	public void setAckType(String ackType) {
		this.ackType = ackType;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Date getAckTime() {
		return ackTime;
	}

	public void setAckTime(Date ackTime) {
		this.ackTime = ackTime;
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

	public String getKeyMon() {
		return keyMon;
	}

	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}

}
