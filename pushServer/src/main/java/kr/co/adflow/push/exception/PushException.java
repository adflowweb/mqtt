package kr.co.adflow.push.exception;

/**
 * @author nadir93
 * @date 2014. 4. 23.
 * 
 */
public class PushException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PushException() {
		super();
	}

	public PushException(String message, Throwable cause) {
		super(message, cause);
	}

	public PushException(String message) {
		super(message);
	}

	public PushException(Throwable cause) {
		super(cause);
	}

}
