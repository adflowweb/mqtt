/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class AckCallback.
 */
public class AckCallback {

	/** The msg id. */
	private String msgId;
	
	/** The ack type. */
	private String ackType;
	
	/** The token id. */
	private String tokenId;
	
	/** The ack time. */
	private Date ackTime;
	
	/** The callback status. */
	private int callbackStatus;
	
	/** The callback count. */
	private int callbackCount;
	
	/** The receiver. */
	private String receiver;
	
	/** The issue id. */
	private String issueId;
	
	/** The callback url. */
	private String callbackUrl;
	
	/** The callback method. */
	private String callbackMethod;
	
	/** The callback count limit. */
	private int callbackCountLimit;
	
	/** The application key. */
	private String applicationKey;
	
	
	/** The ufmi. */
	private String ufmi;
	
	

	public String getUfmi() {
		return ufmi;
	}

	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

	/**
	 * Gets the msg id.
	 *
	 * @return the msg id
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Sets the msg id.
	 *
	 * @param msgId the new msg id
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * Gets the ack type.
	 *
	 * @return the ack type
	 */
	public String getAckType() {
		return ackType;
	}

	/**
	 * Sets the ack type.
	 *
	 * @param ackType the new ack type
	 */
	public void setAckType(String ackType) {
		this.ackType = ackType;
	}

	/**
	 * Gets the token id.
	 *
	 * @return the token id
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * Sets the token id.
	 *
	 * @param tokenId the new token id
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * Gets the ack time.
	 *
	 * @return the ack time
	 */
	public Date getAckTime() {
		return ackTime;
	}

	/**
	 * Sets the ack time.
	 *
	 * @param ackTime the new ack time
	 */
	public void setAckTime(Date ackTime) {
		this.ackTime = ackTime;
	}

	/**
	 * Gets the callback status.
	 *
	 * @return the callback status
	 */
	public int getCallbackStatus() {
		return callbackStatus;
	}

	/**
	 * Sets the callback status.
	 *
	 * @param callbackStatus the new callback status
	 */
	public void setCallbackStatus(int callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	/**
	 * Gets the callback count.
	 *
	 * @return the callback count
	 */
	public int getCallbackCount() {
		return callbackCount;
	}

	/**
	 * Sets the callback count.
	 *
	 * @param callbackCount the new callback count
	 */
	public void setCallbackCount(int callbackCount) {
		this.callbackCount = callbackCount;
	}

	/**
	 * Gets the receiver.
	 *
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Sets the receiver.
	 *
	 * @param receiver the new receiver
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	 * Gets the callback count limit.
	 *
	 * @return the callback count limit
	 */
	public int getCallbackCountLimit() {
		return callbackCountLimit;
	}

	/**
	 * Sets the callback count limit.
	 *
	 * @param callbackCountLimit the new callback count limit
	 */
	public void setCallbackCountLimit(int callbackCountLimit) {
		this.callbackCountLimit = callbackCountLimit;
	}

	/**
	 * Gets the application key.
	 *
	 * @return the application key
	 */
	public String getApplicationKey() {
		return applicationKey;
	}

	/**
	 * Sets the application key.
	 *
	 * @param applicationKey the new application key
	 */
	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	@Override
	public String toString() {
		return "AckCallback [msgId=" + msgId + ", ackType=" + ackType
				+ ", tokenId=" + tokenId + ", ackTime=" + ackTime
				+ ", callbackStatus=" + callbackStatus + ", callbackCount="
				+ callbackCount + ", receiver=" + receiver + ", issueId="
				+ issueId + ", callbackUrl=" + callbackUrl
				+ ", callbackMethod=" + callbackMethod
				+ ", callbackCountLimit=" + callbackCountLimit
				+ ", applicationKey=" + applicationKey + ", ufmi=" + ufmi + "]";
	}

	
	

}
