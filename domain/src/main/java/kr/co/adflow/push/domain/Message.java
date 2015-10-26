/*
 * 
 */
package kr.co.adflow.push.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 *
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class Message {

	/** The Constant STATUS_WAIT_FOR_SENDING. */
	public static final int STATUS_WAIT_FOR_SENDING = 0;
	
	/** The Constant STATUS_PUSH_SENT. */
	public static final int STATUS_PUSH_SENT = 1;
	
	/** The Constant STATUS_SMS_SENT. */
	public static final int STATUS_SMS_SENT = 2;
	
	/** The Constant STATUS_SMS_PROCESS_DONE. */
	public static final int STATUS_SMS_PROCESS_DONE = 3; // 전송작업을 수행하였지만 실제
															// sms 전송건수가 0 일때
	/** The Constant STATUS_EXIST_ACK. */
															public static final int STATUS_EXIST_ACK = 4; // 개인메시지용

	/** The Constant STATUS_PHONENUMBER_NOT_FOUND. */
	public static final int STATUS_PHONENUMBER_NOT_FOUND = 100;
	
	/** The Constant STATUS_USER_NOT_FOUND. */
	public static final int STATUS_USER_NOT_FOUND = 200;

	/** The Constant STATUS_ERROR. */
	public static final int STATUS_ERROR = 500; // 에러

	/** The Constant NOTIFICATION_PERSONAL. */
	public static final int NOTIFICATION_PERSONAL = 0; // 개인메시지
	
	/** The Constant NOTIFICATION_ALL. */
	public static final int NOTIFICATION_ALL = 1; // 전체메시지
	
	/** The Constant NOTIFICATION_GROUP_AFFILIATE. */
	public static final int NOTIFICATION_GROUP_AFFILIATE = 2; // 계엵사 그룹메시지
	
	/** The Constant NOTIFICATION_GROUP_DEPT. */
	public static final int NOTIFICATION_GROUP_DEPT = 3; // 부서 그룹메시지
	
	/** The Constant NOTIFICATION_GROUP_TITLE. */
	public static final int NOTIFICATION_GROUP_TITLE = 4; // 직급 그룹메시지

	/** The Constant COMMAND_SUBSCRIBE. */
	public static final int COMMAND_SUBSCRIBE = 100; // subscribe
	
	/** The Constant COMMAND_UNSUBSCRIBE. */
	public static final int COMMAND_UNSUBSCRIBE = 101; // unsubscribe

	/** The id. */
	private int id;
	
	/** The content. */
	private String content;
	
	/** The sender. */
	private String sender;
	
	/** The receiver. */
	private String receiver;
	
	/** The issue. */
	private Date issue;
	
	/** The issue sms. */
	private Date issueSms;
	
	/** The content type. */
	private String contentType;

	/** quality of service qos0 : at most once qos1 : at least once qos2 : exactly once. */
	private int qos;

	/** mqtt retained. */
	private boolean retained;

	/** sms 발송 여부. */
	private boolean sms;
	
	/** The ack. */
	private boolean ack = false;

	/** 메시지 전송실패 여부 판단시간  sms 발송여부가 true인 경우 pushClient에서 ack 메시지가 timeOut 시간까지 안올경우 sms전송. */
	private int timeOut;
	
	/** The expiry. */
	private long expiry;

	/** 예약발송시간. */
	private Date reservation;

	/** The type. */
	private int type;

	/** The status. */
	private int status;

	/** The service id. */
	private String serviceID;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param sender the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
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
	 * Checks if is retained.
	 *
	 * @return true, if is retained
	 */
	public boolean isRetained() {
		return retained;
	}

	/**
	 * Sets the retained.
	 *
	 * @param retained the new retained
	 */
	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	/**
	 * Checks if is sms.
	 *
	 * @return true, if is sms
	 */
	public boolean isSms() {
		return sms;
	}

	/**
	 * Sets the sms.
	 *
	 * @param sms the new sms
	 */
	public void setSms(boolean sms) {
		this.sms = sms;
	}

	/**
	 * Gets the time out.
	 *
	 * @return the time out
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Sets the time out.
	 *
	 * @param timeOut the new time out
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Gets the reservation.
	 *
	 * @return the reservation
	 */
	public Date getReservation() {
		return reservation;
	}

	/**
	 * Sets the reservation.
	 *
	 * @param reservation the new reservation
	 */
	public void setReservation(Date reservation) {
		this.reservation = reservation;
	}

	/**
	 * Gets the issue.
	 *
	 * @return the issue
	 */
	public Date getIssue() {
		return issue;
	}

	/**
	 * Sets the issue.
	 *
	 * @param issue the new issue
	 */
	public void setIssue(Date issue) {
		this.issue = issue;
	}

	/**
	 * Gets the issue sms.
	 *
	 * @return the issue sms
	 */
	public Date getIssueSms() {
		return issueSms;
	}

	/**
	 * Sets the issue sms.
	 *
	 * @param issueSms the new issue sms
	 */
	public void setIssueSms(Date issueSms) {
		this.issueSms = issueSms;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
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
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * Sets the service id.
	 *
	 * @param serviceID the new service id
	 */
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + ", sender="
				+ sender + ", receiver=" + receiver + ", issue=" + issue
				+ ", issueSms=" + issueSms + ", contentType=" + contentType
				+ ", qos=" + qos + ", retained=" + retained + ", sms=" + sms
				+ ", timeOut=" + timeOut + ", reservation=" + reservation
				+ ", type=" + type + ", status=" + status + ", serviceID="
				+ serviceID + "]";
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

}