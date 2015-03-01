package kr.co.adflow.pms.domain;

import java.util.Date;

public class AckCallback {

	private String msgId;
	private String ackType;
	private String tokenId;
	private Date ackTime;
	private int callbackStatus;
	private int callbackCount;
	private String receiver;
	private String issueId;
	private String callbackUrl;
	private String callbackMethod;
	private int callbackCountLimit;
	private String applicationKey;
	
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
	public int getCallbackStatus() {
		return callbackStatus;
	}
	public void setCallbackStatus(int callbackStatus) {
		this.callbackStatus = callbackStatus;
	}
	public int getCallbackCount() {
		return callbackCount;
	}
	public void setCallbackCount(int callbackCount) {
		this.callbackCount = callbackCount;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
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
	public int getCallbackCountLimit() {
		return callbackCountLimit;
	}
	public void setCallbackCountLimit(int callbackCountLimit) {
		this.callbackCountLimit = callbackCountLimit;
	}
	public String getApplicationKey() {
		return applicationKey;
	}
	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

}
