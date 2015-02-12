package kr.co.adflow.pms.domain;

import java.util.Date;

import kr.co.adflow.pms.core.util.JsonDateSerializer;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageResult {
	
	private String msgId;
	private String receiver;
	private int status;
	private boolean reservation;
	private int resendCount;

	@JsonSerialize(using=JsonDateSerializer.class)
	private Date updateTime;
	
	private String appAckType;
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date appAckTime;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	public int getResendCount() {
		return resendCount;
	}
	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
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
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
