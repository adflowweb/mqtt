package kr.co.adflow.pms.domain;

import java.util.Date;

public class Message {

	private String keyMon;
	private String msgId;
	private int msgType;
	private String receiver;
	private String receiverTopic;
	private String serviceId;
	private boolean ack;
	private int status;
	private boolean reservation;
	private Date reservationTime;
	private int resendCount;
	private int resendMaxCount;
	private int resendInterval;
	private String resendId;
	private String serverId;
	private long expiry;
	private int qos;
	private String retained;
	private Date issueTime;
	private String issueId;
	private Date updateTime;
	private String updateId;
	
	private String pmaAckType;
	private Date pmaAckTime;
	private String appAckType;
	private Date appAckTime;
	
	private String contentType;
	private String content;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public boolean isAck() {
		return ack;
	}
	public void setAck(boolean ack) {
		this.ack = ack;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isReservation() {
		return reservation;
	}
	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}
	public Date getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public long getExpiry() {
		return expiry;
	}
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}
	public int getQos() {
		return qos;
	}
	public void setQos(int qos) {
		this.qos = qos;
	}
	public String getRetained() {
		return retained;
	}
	public void setRetained(String retained) {
		this.retained = retained;
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
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getKeyMon() {
		return keyMon;
	}
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}
	public int getResendCount() {
		return resendCount;
	}
	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}
	public int getResendInterval() {
		return resendInterval;
	}
	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
	}
	public String getReceiverTopic() {
		return receiverTopic;
	}
	public void setReceiverTopic(String receiverTopic) {
		this.receiverTopic = receiverTopic;
	}
	public String getResendId() {
		return resendId;
	}
	public void setResendId(String resendId) {
		this.resendId = resendId;
	}
	public String getPmaAckType() {
		return pmaAckType;
	}
	public void setPmaAckType(String pmaAckType) {
		this.pmaAckType = pmaAckType;
	}
	public Date getPmaAckTime() {
		return pmaAckTime;
	}
	public void setPmaAckTime(Date pmaAckTime) {
		this.pmaAckTime = pmaAckTime;
	}
	public String getAppAckType() {
		return appAckType;
	}
	public void setAppAckType(String appAckType) {
		this.appAckType = appAckType;
	}
	public Date getAppAckTime() {
		return appAckTime;
	}
	public void setAppAckTime(Date appAckTime) {
		this.appAckTime = appAckTime;
	}
	public int getResendMaxCount() {
		return resendMaxCount;
	}
	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
	}


}
