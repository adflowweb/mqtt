///*
// * 
// */
//package kr.co.adflow.pms.response;
//
//import java.util.List;
//
//import org.codehaus.jackson.map.annotate.JsonSerialize;
//
//// TODO: Auto-generated Javadoc
///**
// * The Class Result.
// *
// * @param <T> the generic type
// */
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//public class Result<T> {
//
//	/** The success. */
//	private boolean success;
//
//	/** The data. */
//	private T data;
//
//	/** The errors. */
//	private List<String> errors;
//
//	/** The warnings. */
//	private List<String> warnings;
//
//	/** The info. */
//	private List<String> info;
//
//	/**
//	 * Gets the warnings.
//	 * 
//	 * @return the warnings
//	 */
//	public List<String> getWarnings() {
//		return warnings;
//	}
//
//	/**
//	 * Sets the warnings.
//	 * 
//	 * @param warnings
//	 *            the new warnings
//	 */
//	public void setWarnings(List<String> warnings) {
//		this.warnings = warnings;
//	}
//
//	/**
//	 * Gets the info.
//	 * 
//	 * @return the info
//	 */
//	public List<String> getInfo() {
//		return info;
//	}
//
//	/**
//	 * Sets the info.
//	 * 
//	 * @param info
//	 *            the new info
//	 */
//	public void setInfo(List<String> info) {
//		this.info = info;
//	}
//
//	/**
//	 * Gets the errors.
//	 * 
//	 * @return the errors
//	 */
//	public List<String> getErrors() {
//		return errors;
//	}
//
//	/**
//	 * Sets the errors.
//	 * 
//	 * @param errors
//	 *            the new errors
//	 */
//	public void setErrors(List<String> errors) {
//		this.errors = errors;
//	}
//
//	/**
//	 * Gets the data.
//	 * 
//	 * @return the data
//	 */
//	public T getData() {
//		return data;
//	}
//
//	/**
//	 * Sets the data.
//	 * 
//	 * @param data
//	 *            the new data
//	 */
//	public void setData(T data) {
//		this.data = data;
//	}
//
//	/**
//	 * Checks if is success.
//	 * 
//	 * @return true, if is success
//	 */
//	public boolean isSuccess() {
//		return success;
//	}
//
//	/**
//	 * Sets the success.
//	 * 
//	 * @param success
//	 *            the new success
//	 */
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return "Result [success=" + success + ", data=" + data + ", errors="
//				+ errors + ", warnings=" + warnings + ", info=" + info + "]";
//	}
//
//	// String retVal =
//	// "result:{isSuccessful:true, errors:[], warnings:[], info:[], data:{push:{'available':"
//	// + pushService.isAvailable() + "}}}";
//
// }
