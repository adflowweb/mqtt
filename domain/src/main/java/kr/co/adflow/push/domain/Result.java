package kr.co.adflow.push.domain;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Result<T> {
	private boolean success;
	private T data;
	private List<String> errors;
	private List<String> warnings;
	private List<String> info;

	/**
	 * @return
	 */
	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * @param warnings
	 */
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * @return
	 */
	public List<String> getInfo() {
		return info;
	}

	/**
	 * @param info
	 */
	public void setInfo(List<String> info) {
		this.info = info;
	}

	/**
	 * @return
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", data=" + data + ", errors="
				+ errors + ", warnings=" + warnings + ", info=" + info + "]";
	}

	// String retVal =
	// "result:{isSuccessful:true, errors:[], warnings:[], info:[], data:{push:{'available':"
	// + pushService.isAvailable() + "}}}";

}
