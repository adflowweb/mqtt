/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class SendMessage.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SendMessage {

	/** The send message. */
	private boolean sendMessage;

	/**
	 * Instantiates a new send message.
	 *
	 * @param sendMessage the send message
	 */
	public SendMessage(boolean sendMessage) {
		super();
		this.sendMessage = sendMessage;
	}

	/**
	 * Checks if is send message.
	 *
	 * @return true, if is send message
	 */
	public boolean isSendMessage() {
		return sendMessage;
	}

	/**
	 * Sets the send message.
	 *
	 * @param sendMessage the new send message
	 */
	public void setSendMessage(boolean sendMessage) {
		this.sendMessage = sendMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SendMessageResponseData [sendMessage=" + sendMessage + "]";
	}
}
