package kr.co.adflow.pms.core.exception;

import java.util.HashMap;

public class TokenRunTimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errCode;
	private String errMsg;
	private HashMap<String, String> hashMap;

	public TokenRunTimeException(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public HashMap<String, String> getErrorMsg() {

		HashMap<String, String> errMap = new HashMap<String, String>();

		errMap.put("errCode", this.errCode);
		errMap.put("errMsg", this.errMsg);

		return errMap;

	}
}