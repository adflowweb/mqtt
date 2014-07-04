package kr.co.adflow.push.exception;

/**
 * @author nadir93
 * @date 2014. 4. 23.
 * 
 */
public class NonExistentUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonExistentUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NonExistentUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NonExistentUserException(String message) {
		super(message);
	}

	public NonExistentUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
