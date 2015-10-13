/*
 * 
 */
package kr.co.adflow.pms.domain;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReq.
 */
public class AddressMessage {

	/** The receivers. */
	@NotEmpty
	private String receiver;
	
	/** The content. */
	@NotEmpty
	private String content;
	
	


	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/** The content length. */
	private Integer contentLength;
	

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
	 * @param contentLength the new content Length
	 */
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
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
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}



}
