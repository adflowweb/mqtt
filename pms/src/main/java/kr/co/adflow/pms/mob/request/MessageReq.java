/*
 * 
 */
package kr.co.adflow.pms.mob.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReq.
 */
public class MessageReq {

	/** The receivers. */
	@NotEmpty
	private String receiver;

	/** The content type. */
	@NotEmpty
	private String contentType;

	/** The content. */
	@NotEmpty
	private String content;

	/** The reservation time. */
	private String sender;

	/** The reservation time. */
	private Date reservationTime;

	/** The qos. */
	@Range(min = 0, max = 2)
	private int qos;

	/** The expiry. */
	private int expiry;

	/** The content length. */
	private Integer contentLength;

	/** The mms. */
	private boolean mms;

	public Date getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

	public boolean isMms() {
		return mms;
	}

	public void setMms(boolean mms) {
		this.mms = mms;
	}

	/**
	 * Gets the content Length.
	 * 
	 * @return the content Length
	 */
	public Integer getContentLength() {
		return contentLength;
	}

	/**
	 * Sets the content Length.
	 * 
	 * @param contentLength
	 *            the new content Length
	 */
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param reservationTime
	 *            the new sender
	 */
	public void setSendere(String sender) {
		this.sender = sender;
	}

	/**
	 * Gets the receivers.
	 * 
	 * @return the receivers
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Sets the receivers.
	 * 
	 * @param receivers
	 *            the new receivers
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * Gets the qos.
	 * 
	 * @return the qos
	 */
	public int getQos() {
		return qos;
	}

	/**
	 * Sets the qos.
	 * 
	 * @param qos
	 *            the new qos
	 */
	public void setQos(int qos) {
		this.qos = qos;
	}

	/**
	 * Gets the expiry.
	 * 
	 * @return the expiry
	 */
	public int getExpiry() {
		return expiry;
	}

	/**
	 * Sets the expiry.
	 * 
	 * @param expiry
	 *            the new expiry
	 */
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

}
