/*
 * 
 */
package kr.co.adflow.push.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class NonExistentUserException.
 *
 * @author nadir93
 * @date 2014. 4. 23.
 */
public class NonExistentUserException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new non existent user exception.
	 */
	public NonExistentUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new non existent user exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public NonExistentUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new non existent user exception.
	 *
	 * @param message the message
	 */
	public NonExistentUserException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new non existent user exception.
	 *
	 * @param cause the cause
	 */
	public NonExistentUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
