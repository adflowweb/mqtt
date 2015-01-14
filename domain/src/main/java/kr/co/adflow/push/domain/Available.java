/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Available.
 *
 * @author nadir93
 * @date 2014. 3. 20.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Available {
	
	/** The available. */
	private boolean available;
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new available.
	 *
	 * @param available the available
	 * @param message the message
	 */
	public Available(boolean available, String message) {
		this.available = available;
		this.message = message;
	}

	/**
	 * Checks if is available.
	 *
	 * @return true, if is available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Sets the available.
	 *
	 * @param available the new available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IsAvailableResponseData [available=" + available + ", message="
				+ message + "]";
	}

}
