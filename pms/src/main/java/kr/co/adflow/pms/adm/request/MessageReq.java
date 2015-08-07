/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReq.
 */
public class MessageReq {

	/** The receivers. */
	@NotEmpty
	private String[] receivers;

	/** The content type. */
	@NotEmpty
	private String contentType;

	/** The content. */
	@NotEmpty
	private String content;

	/** The reservation time. */
	private Date reservationTime;

	/** The resend max count. */
	@Range(min = 0, max = 100)
	private int resendMaxCount;

	/** The resend interval. */
	@Range(min = 0, max = 1440)
	private int resendInterval;

	/** The msg type. */
	private int msgType;

	/** The service id. */
	@NotEmpty
	private String serviceId;

	/** The content length. */
	private Integer contentLength;

	/** The file Name. */
	private String fileName;

	/** The file Format. */
	private String fileFormat;

	/** The mms. */
	private boolean mms;

	/** The ack. */
	private boolean ack;

	/** The expiry. */
	private long expiry;

	/** The qos. */
	private int qos;

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

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	public boolean isMms() {
		return mms;
	}

	public void setMms(boolean mms) {
		this.mms = mms;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
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
	 * @param reservationTime
	 *            the new reservation time
	 */
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

	/**
	 * Gets the receivers.
	 * 
	 * @return the receivers
	 */
	public String[] getReceivers() {
		return receivers;
	}

	/**
	 * Sets the receivers.
	 * 
	 * @param receivers
	 *            the new receivers
	 */
	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}

	/**
	 * Gets the resend interval.
	 * 
	 * @return the resend interval
	 */
	public int getResendInterval() {
		return resendInterval;
	}

	/**
	 * Sets the resend interval.
	 * 
	 * @param resendInterval
	 *            the new resend interval
	 */
	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
	}

	/**
	 * Gets the resend max count.
	 * 
	 * @return the resend max count
	 */
	public int getResendMaxCount() {
		return resendMaxCount;
	}

	/**
	 * Sets the resend max count.
	 * 
	 * @param resendMaxCount
	 *            the new resend max count
	 */
	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
	}

}
