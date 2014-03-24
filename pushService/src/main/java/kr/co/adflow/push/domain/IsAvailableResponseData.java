package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class IsAvailableResponseData extends ResponseData {
	private boolean available;
	private String message;

	public IsAvailableResponseData() {
		super();
	}

	/**
	 * @param available
	 * @param message
	 */
	public IsAvailableResponseData(boolean available, String message) {
		this.available = available;
		this.message = message;
	}

	/**
	 * @return
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

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

	@Override
	public String toString() {
		return "IsAvailableResponseData [available=" + available + ", message="
				+ message + "]";
	}

}
