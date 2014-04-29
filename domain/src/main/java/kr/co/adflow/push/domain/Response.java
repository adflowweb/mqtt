package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class Response<T> {

	private Result<T> result;

	public Response() {
	}

	/**
	 * @param result
	 */
	public Response(Result<T> result) {
		this.result = result;
	}

	/**
	 * @return
	 */
	public Result<T> getResult() {
		return result;
	}

	/**
	 * @param result
	 */
	public void setResult(Result<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Response [result=" + result + "]";
	}

}
