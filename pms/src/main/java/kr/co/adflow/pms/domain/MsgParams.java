package kr.co.adflow.pms.domain;

import java.util.Date;

public class MsgParams {
	
	private String keyMon;
	private String issueId;
	private int iDisplayStart;
	private int iDisplayLength;
	
	private Date dateStart;
	
	private Date dateEnd;
	
	private String[] statusArray;
	
	private int ackType;
	
	private String receiver;
	
	private String msgId;
	
	public String getKeyMon() {
		return keyMon;
	}
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String[] getStatusArray() {
		return statusArray;
	}
	public void setStatusArray(String[] statusArray) {
		this.statusArray = statusArray;
	}
	public int getAckType() {
		return ackType;
	}
	public void setAckType(int ackType) {
		this.ackType = ackType;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

}
