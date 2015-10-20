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

	/** The msg id. */
	private String msgId;

	/** The msg type. */
	private int msgType;

	/** The receiver. */
	private String receiver;

	/** The service id. */
	private String serviceId;

	/** The ack. */
	private boolean ack;

	/** The status. */
	private int status;

	/** The server id. */
	private String serverId;

	/** The expiry. */
	private long expiry;

	/** The qos. */
	private int qos;

	/** The issue time. */
	private Date issueTime;

	/** The issue id. */
	private String issueId;

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
	 * @param msgId
	 *            the new msg id
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
	 * @param msgType
	 *            the new msg type
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
	 * @param receiver
	 *            the new receiver
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
	 * @param serviceId
	 *            the new service id
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
	 * @param ack
	 *            the new ack
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
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status) {
		this.status = status;
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
	 * @param serverId
	 *            the new server id
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
	 * @param expiry
	 *            the new expiry
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
	 * @param qos
	 *            the new qos
	 */
	public void setQos(int qos) {
		this.qos = qos;
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
	 * @param issueTime
	 *            the new issue time
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
	 * @param issueId
	 *            the new issue id
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
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
	 * @param contentType
	 *            the new content type
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
	 * @param content
	 *            the new content
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @param pmaAckType
	 *            the new pma ack type
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
	 * @param pmaAckTime
	 *            the new pma ack time
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
	 * @param appAckType
	 *            the new app ack type
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
	 * @param appAckTime
	 *            the new app ack time
	 */
	public void setAppAckTime(Date appAckTime) {
		this.appAckTime = appAckTime;
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
	 * @param msgSize
	 *            the new msgSize
	 */
	public void setMsgSize(int msgSize) {
		this.msgSize = msgSize;
	}

	@Override
	public String toString() {
		return "Message [msgId=" + msgId + ", msgType=" + msgType
				+ ", receiver=" + receiver + ", serviceId=" + serviceId
				+ ", ack=" + ack + ", status=" + status + ", serverId="
				+ serverId + ", expiry=" + expiry + ", qos=" + qos
				+ ", issueTime=" + issueTime + ", issueId=" + issueId
				+ ", pmaAckType=" + pmaAckType + ", pmaAckTime=" + pmaAckTime
				+ ", appAckType=" + appAckType + ", appAckTime=" + appAckTime
				+ ", contentType=" + contentType + ", content=" + content
				+ ", msgSize=" + msgSize + "]";
	}

	

}
