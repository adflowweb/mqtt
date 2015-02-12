package kr.co.adflow.pms.adm.request;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class MessageReq {

	@NotEmpty
	private String[] receivers;
	@NotEmpty
	private String contentType;
	@NotEmpty
	private String content;
	private Date reservationTime;
	private int resendMaxCount;
	private int resendInterval;
	
	// admin 수정 가능
	
	private int msgType;
	@NotEmpty
	private String serviceId;
	
	private boolean ack;
	
	private long expiry;
	
	private int qos;


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

	public Date getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

	public String[] getReceivers() {
		return receivers;
	}

	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}



	public int getResendInterval() {
		return resendInterval;
	}

	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
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

	public int getResendMaxCount() {
		return resendMaxCount;
	}

	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
	}

}
