package kr.co.adflow.push.exception;

public class PhoneNumberNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PhoneNumberNotFoundException() {
		super();
	}

	public PhoneNumberNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PhoneNumberNotFoundException(String message) {
		super(message);
	}

	public PhoneNumberNotFoundException(Throwable cause) {
		super(cause);
	}
}
