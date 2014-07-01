package kr.co.adflow.push.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class Message {

	public static int NOTIFICATION = 0;
	public static int COMMAND = 1;

	private int id;
	private String content;
	private String sender;
	private String receiver;
	private Date issue;
	private Date issueSms;

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

	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + ", sender="
				+ sender + ", receiver=" + receiver + ", issue=" + issue
				+ ", issueSms=" + issueSms + ", qos=" + qos + ", retained="
				+ retained + ", sms=" + sms + ", timeOut=" + timeOut
				+ ", reservation=" + reservation + ", type=" + type + "]";
	}

}