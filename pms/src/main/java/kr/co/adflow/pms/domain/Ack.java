/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Ack.
 */
public class Ack {

	/** The key mon. */
	private String keyMon;
	
	/** The msg id. */
	private String msgId;
	
	/** The ack type. */
	private String ackType;
	
	/** The token id. */
	private String tokenId;
	
	/** The ack time. */
	private Date ackTime;
	
	/** The issue time. */
	private Date issueTime;
	
	/** The issue id. */
	private String issueId;
	
	/** The server id. */
	private String serverId;

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
	 * Gets the issue time.
	 *
	 * @return the issue time
	 */
	public Date getIssueTime() {
		return issueTime;
	}

	/**
	 * Sets the issue time.
	 *
	 * @param issueTime the new issue time
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
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
	 * Gets the key mon.
	 *
	 * @return the key mon
	 */
	public String getKeyMon() {
		return keyMon;
	}

	/**
	 * Sets the key mon.
	 *
	 * @param keyMon the new key mon
	 */
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}

	/**
	 * Gets the server id.
	 *
	 * @return the server id
	 */
	public String getServerId() {
		return serverId;
	}

	/**
	 * Sets the server id.
	 *
	 * @param serverId the new server id
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
