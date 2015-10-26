/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Result.
 * 
 * @param <T>
 *            the generic type
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Response<T> {

	/** The data. */

	private String status;
	private T data;
	private String code;
	private String message;
	private String explaination;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExplaination() {
		return explaination;
	}

	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", data=" + data + ", code="
				+ code + ", message=" + message + ", explaination="
				+ explaination + "]";
	}

}
