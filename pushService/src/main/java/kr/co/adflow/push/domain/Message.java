package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
abstract public class Message {
	private String message;
	private String sender;

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	// @Override
	// public String toString() {
	// return "Message [message=" + message + ", sender=" + sender + "]";
	// }

}
