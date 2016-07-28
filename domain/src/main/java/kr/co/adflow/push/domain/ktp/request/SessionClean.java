package kr.co.adflow.push.domain.ktp.request;

public class SessionClean {
	/** The sender. */
	private String sender;

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/** The receiver. */
	private String receiver;

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
	 * @param sender
	 *            the new sender
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
	 * @param receiver
	 *            the new receiver
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
