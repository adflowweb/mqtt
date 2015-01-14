/*
 * 
 */
package kr.co.adflow.push.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class PhoneNumberNotFoundException.
 */
public class PhoneNumberNotFoundException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new phone number not found exception.
	 */
	public PhoneNumberNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new phone number not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public PhoneNumberNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new phone number not found exception.
	 *
	 * @param message the message
	 */
	public PhoneNumberNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new phone number not found exception.
	 *
	 * @param cause the cause
	 */
	public PhoneNumberNotFoundException(Throwable cause) {
		super(cause);
	}
}
