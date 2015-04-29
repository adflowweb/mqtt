/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class CDR {
	
	/** The  */
	private Date updateTime;
	
	/** The  */
	private Date pmaAckTime;
	
	/** The  */
	private String issueId;
	
	/** The  */
	private String receiverTopic;
	
	/** The  */
	private int mediaType;
	
	/** The sendTerminalType */
	private int sendTerminalType;
	
	/** The msgSize */
	private int msgSize;
	
	/** The groupId */
	private String groupId;
	
	/** The receiverUfmi */
	private String receiverUfmi;

	
	/**
	 * Gets the receiver Ufmi.
	 *
	 * @return the receiverUfmi
	 */
	public String getReceiverUfmi() {
		return receiverUfmi;
	}

	/**
	 * Sets the receiver Ufmi.
	 *
	 * @param receiverUfmi the new receiver Ufmi
	 */
	public void setReceiverUfmi(String receiverUfmi) {
		this.receiverUfmi = receiverUfmi;
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
	 * Gets the update time.
	 *
	 * @return the update time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * Sets the update time.
	 *
	 * @param updateTime the new update time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Gets the receiver topic.
	 *
	 * @return the receiver topic
	 */
	public String getReceiverTopic() {
		return receiverTopic;
	}

	/**
	 * Sets the receiver topic.
	 *
	 * @param receiverTopic the new receiver topic
	 */
	public void setReceiverTopic(String receiverTopic) {
		this.receiverTopic = receiverTopic;
	}


	/**
	 * Gets the pma ack time.
	 *
	 * @return the pma ack time
	 */
	public Date getPmaAckTime() {
		return pmaAckTime;
	}

	/**
	 * Sets the pma ack time.
	 *
	 * @param pmaAckTime the new pma ack time
	 */
	public void setPmaAckTime(Date pmaAckTime) {
		this.pmaAckTime = pmaAckTime;
	}

	/**
	 * Gets the group id.
	 *
	 * @return the group id
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Sets the group id.
	 *
	 * @param groupId the new group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * Sets the mediaType.
	 *
	 * @return the mediaType
	 */
	public int getMediaType() {
		return mediaType;
	}

	/**
	 * Sets the mediaType.
	 *
	 * @param mediaType the new mediaType
	 */
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * Sets the sendTerminalType.
	 *
	 * @return the sendTerminalType
	 */
	public int getSendTerminalType() {
		return sendTerminalType;
	}

	/**
	 * Sets the sendTerminalType.
	 *
	 * @param sendTerminalType the new sendTerminalType
	 */
	public void setSendTerminalType(int sendTerminalType) {
		this.sendTerminalType = sendTerminalType;
	}

	/**
	 * Sets the msgSize.
	 *
	 * @return the msgSize
	 */
	public int getMsgSize() {
		return msgSize;
	}

	/**
	 * Sets the msgSize.
	 *
	 * @param msgSize the new msgSize
	 */
	public void setMsgSize(int msgSize) {
		this.msgSize = msgSize;
	}

}
