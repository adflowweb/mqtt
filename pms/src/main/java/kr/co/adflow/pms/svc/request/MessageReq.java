package kr.co.adflow.pms.svc.request;

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
	private int resendCount;
	private int resendInterval;

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
