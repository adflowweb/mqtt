/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class MsgParams.
 */
public class MsgParams {

	/** The key mon. */
	private String keyMon;
	
	/** The issue id. */
	private String issueId;
	
	/** The i display start. */
	private int iDisplayStart;
	
	/** The i display length. */
	private int iDisplayLength;

	/** The date start. */
	private Date dateStart;

	/** The date end. */
	private Date dateEnd;

	/** The status array. */
	private String[] statusArray;

	/** The ack type. */
	private int ackType;

	/** The receiver. */
	private String receiver;

	/** The msg id. */
	private String msgId;
	
	/** The msg type. */
	private int msgType;



	/**
	 * Gets the key mon.
	 *
	 * @return the key mon
	 */
	public String getKeyMon() {
		return keyMon;
	}

	/**
	 * Sets the key mon.
	 *
	 * @param keyMon the new key mon
	 */
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}

	/**
	 * Gets the issue id.
	 *
	 * @return the issue id
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * Sets the issue id.
	 *
	 * @param issueId the new issue id
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	/**
	 * Gets the i display start.
	 *
	 * @return the i display start
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * Sets the i display start.
	 *
	 * @param iDisplayStart the new i display start
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * Gets the i display length.
	 *
	 * @return the i display length
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * Sets the i display length.
	 *
	 * @param iDisplayLength the new i display length
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * Gets the date start.
	 *
	 * @return the date start
	 */
	public Date getDateStart() {
		return dateStart;
	}

	/**
	 * Sets the date start.
	 *
	 * @param dateStart the new date start
	 */
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	/**
	 * Gets the date end.
	 *
	 * @return the date end
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * Sets the date end.
	 *
	 * @param dateEnd the new date end
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * Gets the status array.
	 *
	 * @return the status array
	 */
	public String[] getStatusArray() {
		return statusArray;
	}

	/**
	 * Sets the status array.
	 *
	 * @param statusArray the new status array
	 */
	public void setStatusArray(String[] statusArray) {
		this.statusArray = statusArray;
	}

	/**
	 * Gets the ack type.
	 *
	 * @return the ack type
	 */
	public int getAckType() {
		return ackType;
	}

	/**
	 * Sets the ack type.
	 *
	 * @param ackType the new ack type
	 */
	public void setAckType(int ackType) {
		this.ackType = ackType;
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
	 * Gets the msg id.
	 *
	 * @return the msg id
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Sets the msg id.
	 *
	 * @param msgId the new msg id
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	/**
	 * Gets the msg type.
	 *
	 * @return the msg type
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * Sets the msg type.
	 *
	 * @param msgType the new msg type
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

}
