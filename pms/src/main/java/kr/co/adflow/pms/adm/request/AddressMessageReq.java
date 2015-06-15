/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import kr.co.adflow.pms.domain.AddressMessage;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReq.
 */
public class AddressMessageReq {

	/** The receivers. */
	@NotEmpty
	private AddressMessage[] addressMessageArray;
	
	/** The content type. */
	@NotEmpty
	private String contentType;
	
	/** The reservation time. */
	private Date reservationTime;
	
	/** The resend max count. */
	private int resendMaxCount;
	
	/** The resend interval. */
	private int resendInterval;

	// admin 수정 가능

	/** The msg type. */
	private int msgType;
	
	/** The service id. */
	@NotEmpty
	private String serviceId;

	/** The ack. */
	private boolean ack;

	/** The expiry. */
	private long expiry;

	/** The qos. */
	private int qos;
	
	/** The file Name. */
	private String fileName;
	
	/** The file Format. */
	private String fileFormat;
	
	/** The mms. */
	private  boolean mms;


	
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

	public AddressMessage[] getAddressMessageArray() {
		return addressMessageArray;
	}

	public void setAddressMessageArray(AddressMessage[] addressMessageArray) {
		this.addressMessageArray = addressMessageArray;
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
	 * @param contentType the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	 * @param resendInterval the new resend interval
	 */
	public void setResendInterval(int resendInterval) {
		this.resendInterval = resendInterval;
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

	/**
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the service id.
	 *
	 * @param serviceId the new service id
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Checks if is ack.
	 *
	 * @return true, if is ack
	 */
	public boolean isAck() {
		return ack;
	}

	/**
	 * Sets the ack.
	 *
	 * @param ack the new ack
	 */
	public void setAck(boolean ack) {
		this.ack = ack;
	}

	/**
	 * Gets the expiry.
	 *
	 * @return the expiry
	 */
	public long getExpiry() {
		return expiry;
	}

	/**
	 * Sets the expiry.
	 *
	 * @param expiry the new expiry
	 */
	public void setExpiry(long expiry) {
		this.expiry = expiry;
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
	 * @param qos the new qos
	 */
	public void setQos(int qos) {
		this.qos = qos;
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
	 * @param resendMaxCount the new resend max count
	 */
	public void setResendMaxCount(int resendMaxCount) {
		this.resendMaxCount = resendMaxCount;
	}

}
