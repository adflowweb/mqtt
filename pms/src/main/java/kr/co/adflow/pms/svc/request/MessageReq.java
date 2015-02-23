package kr.co.adflow.pms.svc.request;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

public class MessageReq {

	@NotEmpty
	private String[] receivers;
	@NotEmpty
	private String contentType;
	@NotEmpty
	private String content;
	private Date reservationTime;
	
	@Range(min=0,max=100)
	private int resendMaxCount;
	@Range(min=0,max=1440)
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



	public int getResendInterval() {
		return resendInterval;
	}

	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
	}

	public int getResendMaxCount() {
		return resendMaxCount;
	}

	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
	}

}
