package kr.co.adflow.push.domain;

import java.util.Date;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class Message {

	public static final int STATUS_WAIT_FOR_SENDING = 0;
	public static final int STATUS_PUSH_SENT = 1;
	public static final int STATUS_SMS_SENT = 2;
	public static final int STATUS_SMS_PROCESS_DONE = 3; // 전송작업을 수행하였지만 실제
															// sms 전송건수가 0 일때
	public static final int STATUS_EXIST_ACK = 4; // 개인메시지용

	public static final int STATUS_PHONENUMBER_NOT_FOUND = 100;
	public static final int STATUS_USER_NOT_FOUND = 200;

	public static final int STATUS_ERROR = 500; // 에러

	public static final int NOTIFICATION_PERSONAL = 0; // 개인메시지
	public static final int NOTIFICATION_ALL = 1; // 전체메시지
	public static final int NOTIFICATION_GROUP_AFFILIATE = 2; // 계엵사 그룹메시지
	public static final int NOTIFICATION_GROUP_DEPT = 3; // 부서 그룹메시지
	public static final int NOTIFICATION_GROUP_TITLE = 4; // 직급 그룹메시지

	public static final int COMMAND_SUBSCRIBE = 100; // subscribe
	public static final int COMMAND_UNSUBSCRIBE = 101; // unsubscribe

	private int id;
	private String content;
	private String sender;
	private String receiver;
	private Date issue;
	private Date issueSms;
	private String contentType;

	/**
	 * quality of service qos0 : at most once qos1 : at least once qos2 :
	 * exactly once
	 */
	private int qos;

	/**
	 * mqtt retained
	 */
	private boolean retained;

	/**
	 * sms 발송 여부
	 */
	private boolean sms;

	/**
	 * 메시지 전송실패 여부 판단시간
	 * 
	 * sms 발송여부가 true인 경우 pushClient에서 ack 메시지가 timeOut 시간까지 안올경우 sms전송
	 */
	private int timeOut;

	/**
	 * 예약발송시간
	 */
	private Date reservation;

	private int type;

	private int status;

	private String serviceID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public Date getReservation() {
		return reservation;
	}

	public void setReservation(Date reservation) {
		this.reservation = reservation;
	}

	public Date getIssue() {
		return issue;
	}

	public void setIssue(Date issue) {
		this.issue = issue;
	}

	public Date getIssueSms() {
		return issueSms;
	}

	public void setIssueSms(Date issueSms) {
		this.issueSms = issueSms;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

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

}