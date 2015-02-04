package kr.co.adflow.pms.svc.request;

import java.util.Date;

public class MessageReq {

	private String[] receivers;
	private String contentType;
	private String content;
	private Date reservationTime;
	private int resendCount;
	private int resendInterval;
	private boolean ack;

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

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
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

}
