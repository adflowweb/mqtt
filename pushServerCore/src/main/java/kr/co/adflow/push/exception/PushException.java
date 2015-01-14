/*
 * 
 */
package kr.co.adflow.push.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class PushException.
 *
 * @author nadir93
 * @date 2014. 4. 23.
 */
public class PushException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new push exception.
	 */
	public PushException() {
		super();
	}

	/**
	 * Instantiates a new push exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public PushException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new push exception.
	 *
	 * @param message the message
	 */
	public PushException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new push exception.
	 *
	 * @param cause the cause
	 */
	public PushException(Throwable cause) {
		super(cause);
	}

}
