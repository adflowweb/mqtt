/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

import kr.co.adflow.pms.core.util.JsonDateSerializer;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageResult.
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageResult {

	/** The msg id. */
	private String msgId;
	
	/** The receiver. */
	private String receiver;
	
	/** The status. */
	private int status;
	
	/** The reservation. */
	private boolean reservation;
	
	/** The reservation time. */
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL, using = JsonDateSerializer.class)
	private Date reservationTime;

	// private int resendCount;

	/** The update time. */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date updateTime;

	/** The pma ack type. */
	private String pmaAckType;
	
	/** The pma ack time. */
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL, using = JsonDateSerializer.class)
	private Date pmaAckTime;

	/** The app ack type. */
	private String appAckType;
	
	/** The app ack time. */
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL, using = JsonDateSerializer.class)
	private Date appAckTime;
	

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
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Checks if is reservation.
	 *
	 * @return true, if is reservation
	 */
	public boolean isReservation() {
		return reservation;
	}

	/**
	 * Sets the reservation.
	 *
	 * @param reservation the new reservation
	 */
	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}

	/**
	 * Gets the app ack type.
	 *
	 * @return the app ack type
	 */
	public String getAppAckType() {
		return appAckType;
	}

	/**
	 * Sets the app ack type.
	 *
	 * @param appAckType the new app ack type
	 */
	public void setAppAckType(String appAckType) {
		this.appAckType = appAckType;
	}

	/**
	 * Gets the app ack time.
	 *
	 * @return the app ack time
	 */
	public Date getAppAckTime() {
		return appAckTime;
	}

	/**
	 * Sets the app ack time.
	 *
	 * @param appAckTime the new app ack time
	 */
	public void setAppAckTime(Date appAckTime) {
		this.appAckTime = appAckTime;
	}

	/**
	 * Gets the update time.
	 *
	 * @return the update time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * Sets the update time.
	 *
	 * @param updateTime the new update time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Gets the pma ack type.
	 *
	 * @return the pma ack type
	 */
	public String getPmaAckType() {
		return pmaAckType;
	}

	/**
	 * Sets the pma ack type.
	 *
	 * @param pmaAckType the new pma ack type
	 */
	public void setPmaAckType(String pmaAckType) {
		this.pmaAckType = pmaAckType;
	}

	/**
	 * Gets the pma ack time.
	 *
	 * @return the pma ack time
	 */
	public Date getPmaAckTime() {
		return pmaAckTime;
	}

	/**
	 * Sets the pma ack time.
	 *
	 * @param pmaAckTime the new pma ack time
	 */
	public void setPmaAckTime(Date pmaAckTime) {
		this.pmaAckTime = pmaAckTime;
	}

	/**
	 * Gets the reservation time.
	 *
	 * @return the reservation time
	 */
	public Date getReservationTime() {
		return reservationTime;
	}

	/**
	 * Sets the reservation time.
	 *
	 * @param reservationTime the new reservation time
	 */
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

}
