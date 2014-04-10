package kr.co.adflow.push.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@XmlRootElement
public class Response {

	private Result result;

	public Response() {
	}

	/**
	 * @param result
	 */
	public Response(Result result) {
		this.result = result;
	}

	/**
	 * @return
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @param result
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Response [result=" + result + "]";
	}

}
