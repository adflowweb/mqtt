/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class Message {

	/** The key mon. */
	private String keyMon;
	
	/** The group id. */
	private String groupId;
	
	/** The msg id. */
	private String msgId;
	
	/** The msg type. */
	private int msgType;
	
	/** The receiver. */
	private String receiver;
	
	/** The receiver topic. */
	private String receiverTopic;
	
	/** The service id. */
	private String serviceId;
	
	/** The ack. */
	private boolean ack;
	
	/** The status. */
	private int status;
	
	/** The reservation. */
	private boolean reservation;
	
	/** The reservation time. */
	private Date reservationTime;
	
	/** The resend count. */
	private int resendCount;
	
	/** The resend max count. */
	private int resendMaxCount;
	
	/** The resend interval. */
	private int resendInterval;
	
	/** The resend id. */
	private String resendId;
	
	/** The server id. */
	private String serverId;
	
	/** The expiry. */
	private long expiry;
	
	/** The qos. */
	private int qos;
	
	/** The retained. */
	private int retained;
	
	/** The issue time. */
	private Date issueTime;
	
	/** The issue id. */
	private String issueId;
	
	private String issueName;
	
	/** The update time. */
	private Date updateTime;
	
	/** The update id. */
	private String updateId;

	/** The pma ack type. */
	private String pmaAckType;
	
	/** The pma ack time. */
	private Date pmaAckTime;
	
	/** The app ack type. */
	private String appAckType;
	
	/** The app ack time. */
	private Date appAckTime;

	/** The content type. */
	private String contentType;
	
	/** The content. */
	private String content;
	
	/** The mediaType. */
	private int mediaType;
	
	/** The sendTerminalType. */
	private int sendTerminalType;
	
	/** The msgSize. */
	private int msgSize;
	
	

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
	 * Gets the msg type.
	 *
	 * @return the msg type
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * Sets the msg type.
	 *
	 * @param msgType the new msg type
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
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
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the service id.
	 *
	 * @param serviceId the new service id
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Checks if is ack.
	 *
	 * @return true, if is ack
	 */
	public boolean isAck() {
		return ack;
	}

	/**
	 * Sets the ack.
	 *
	 * @param ack the new ack
	 */
	public void setAck(boolean ack) {
		this.ack = ack;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Checks if is reservation.
	 *
	 * @return true, if is reservation
	 */
	public boolean isReservation() {
		return reservation;
	}

	/**
	 * Sets the reservation.
	 *
	 * @param reservation the new reservation
	 */
	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}

	/**
	 * Gets the reservation time.
	 *
	 * @return the reservation time
	 */
	public Date getReservationTime() {
		return reservationTime;
	}

	/**
	 * Sets the reservation time.
	 *
	 * @param reservationTime the new reservation time
	 */
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
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

	/**
	 * Gets the expiry.
	 *
	 * @return the expiry
	 */
	public long getExpiry() {
		return expiry;
	}

	/**
	 * Sets the expiry.
	 *
	 * @param expiry the new expiry
	 */
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	/**
	 * Gets the qos.
	 *
	 * @return the qos
	 */
	public int getQos() {
		return qos;
	}

	/**
	 * Sets the qos.
	 *
	 * @param qos the new qos
	 */
	public void setQos(int qos) {
		this.qos = qos;
	}

	/**
	 * Gets the retained.
	 *
	 * @return the retained
	 */
	public int getRetained() {
		return retained;
	}

	/**
	 * Sets the retained.
	 *
	 * @param retained the new retained
	 */
	public void setRetained(int retained) {
		this.retained = retained;
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
	 * Gets the update id.
	 *
	 * @return the update id
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * Sets the update id.
	 *
	 * @param updateId the new update id
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * Gets the resend count.
	 *
	 * @return the resend count
	 */
	public int getResendCount() {
		return resendCount;
	}

	/**
	 * Sets the resend count.
	 *
	 * @param resendCount the new resend count
	 */
	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}

	/**
	 * Gets the resend interval.
	 *
	 * @return the resend interval
	 */
	public int getResendInterval() {
		return resendInterval;
	}

	/**
	 * Sets the resend interval.
	 *
	 * @param resendInterval the new resend interval
	 */
	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
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
	 * Gets the resend id.
	 *
	 * @return the resend id
	 */
	public String getResendId() {
		return resendId;
	}

	/**
	 * Sets the resend id.
	 *
	 * @param resendId the new resend id
	 */
	public void setResendId(String resendId) {
		this.resendId = resendId;
	}

	/**
	 * Gets the pma ack type.
	 *
	 * @return the pma ack type
	 */
	public String getPmaAckType() {
		return pmaAckType;
	}

	/**
	 * Sets the pma ack type.
	 *
	 * @param pmaAckType the new pma ack type
	 */
	public void setPmaAckType(String pmaAckType) {
		this.pmaAckType = pmaAckType;
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
	 * Gets the app ack type.
	 *
	 * @return the app ack type
	 */
	public String getAppAckType() {
		return appAckType;
	}

	/**
	 * Sets the app ack type.
	 *
	 * @param appAckType the new app ack type
	 */
	public void setAppAckType(String appAckType) {
		this.appAckType = appAckType;
	}

	/**
	 * Gets the app ack time.
	 *
	 * @return the app ack time
	 */
	public Date getAppAckTime() {
		return appAckTime;
	}

	/**
	 * Sets the app ack time.
	 *
	 * @param appAckTime the new app ack time
	 */
	public void setAppAckTime(Date appAckTime) {
		this.appAckTime = appAckTime;
	}

	/**
	 * Gets the resend max count.
	 *
	 * @return the resend max count
	 */
	public int getResendMaxCount() {
		return resendMaxCount;
	}

	/**
	 * Sets the resend max count.
	 *
	 * @param resendMaxCount the new resend max count
	 */
	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
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
	 * Sets the getIssueName.
	 *
	 * @return the group
	 */
	public String getIssueName() {
		return issueName;
	}

	/**
	 * Sets the getIssueName.
	 *
	 * @param getIssueName the new getIssueName
	 */
	public void setIssueName(String issueName) {
		this.issueName = issueName;
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

	@Override
	public String toString() {
		return "Message [keyMon=" + keyMon + ", groupId=" + groupId
				+ ", msgId=" + msgId + ", msgType=" + msgType + ", receiver="
				+ receiver + ", receiverTopic=" + receiverTopic
				+ ", serviceId=" + serviceId + ", ack=" + ack + ", status="
				+ status + ", reservation=" + reservation
				+ ", reservationTime=" + reservationTime + ", resendCount="
				+ resendCount + ", resendMaxCount=" + resendMaxCount
				+ ", resendInterval=" + resendInterval + ", resendId="
				+ resendId + ", serverId=" + serverId + ", expiry=" + expiry
				+ ", qos=" + qos + ", retained=" + retained + ", issueTime="
				+ issueTime + ", issueId=" + issueId + ", issueName="
				+ issueName + ", updateTime=" + updateTime + ", updateId="
				+ updateId + ", pmaAckType=" + pmaAckType + ", pmaAckTime="
				+ pmaAckTime + ", appAckType=" + appAckType + ", appAckTime="
				+ appAckTime + ", contentType=" + contentType + ", content="
				+ content + ", mediaType=" + mediaType + ", sendTerminalType="
				+ sendTerminalType + ", msgSize=" + msgSize + "]";
	}

}
