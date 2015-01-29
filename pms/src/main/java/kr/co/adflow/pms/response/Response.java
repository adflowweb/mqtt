/*
 * 
 */
package kr.co.adflow.pms.response;

public class Response<T> {

	/** The result. */
	private Result<T> result;

	// /**
	// * Instantiates a new response.
	// */
	// public Response() {
	// }

	/**
	 * Instantiates a new response.
	 * 
	 * @param result
	 *            the result
	 */
	public Response(Result<T> result) {
		this.result = result;
	}

	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	public Result<T> getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 * 
	 * @param result
	 *            the new result
	 */
	public void setResult(Result<T> result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Response [result=" + result + "]";
	}

}
