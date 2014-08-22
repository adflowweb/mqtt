package kr.co.adflow.push.domain;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Token {

	private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private String tokenID;
	private String userID;
	private Date issue;
	private Role[] role;

	public Token() {
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getIssue() {
		// return sdf.format(issue);
		return issue;
	}

	public void setIssue(Date issue) {
		this.issue = issue;
	}

	public Role[] getRole() {
		return role;
	}

	public void setRole(Role[] role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Token [tokenID=" + tokenID + ", userID=" + userID
				+ ", issue=" + issue + ", role="
				+ Arrays.toString(role) + "]";
	}

}
