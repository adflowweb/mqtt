package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TokenResponseData extends ResponseData {

	private boolean validation;
	private String userID;
	private String token;

	public TokenResponseData() {
	}

	public TokenResponseData(boolean validation, String userID) {
		super();
		this.validation = validation;
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TokenResponseData [validation=" + validation + ", userID="
				+ userID + ", token=" + token + "]";
	}
}