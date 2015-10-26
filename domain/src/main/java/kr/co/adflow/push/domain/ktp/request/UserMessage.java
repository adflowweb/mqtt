/*
 * 
 */
package kr.co.adflow.push.domain.ktp.request;


// TODO: Auto-generated Javadoc
/**
 * The Class UserMessage.
 */
public class UserMessage {

	/** The content. */
	private String content;
	
	/** The sender. */
	private String sender;
	
	/** The receiver. */
	private String receiver;
	
	/** The content type. */
	private String contentType;
	
	/** The qos. */
	private int qos;
	
	/** The expiry. */
	private int expiry;
	//private int type;
	
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
	 * @param content the new content
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
	 * @param sender the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
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
	 * @param expiry the new expiry
	 */
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}


}