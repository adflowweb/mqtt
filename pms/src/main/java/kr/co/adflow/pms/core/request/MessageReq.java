/*
 * 
 */
package kr.co.adflow.pms.core.request;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReq.
 */
public class MessageReq {

	/** The receivers. */

	private String receiver;

	/** The content type. */

	private String contentType;

	/** The content. */

	private String content;

	/** The msg type. */
	private int msgType;

	private String serviceId;

	/** The content length. */
	private Integer contentLength;

	/** The expiry. */
	private long expiry;

	/** The qos. */
	private int qos;
	
	private boolean ack;
	
	



	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

}
