/*
 * 
 */
package kr.co.adflow.push.domain;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Token.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Token {

	/** The sdf. */
	private static SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** The token id. */
	private String tokenID;
	
	/** The user id. */
	private String userID;
	
	/** The issue. */
	private Date issue;
	
	/** The role. */
	private Role[] role;

	/**
	 * Instantiates a new token.
	 */
	public Token() {
	}

	/**
	 * Gets the token id.
	 *
	 * @return the token id
	 */
	public String getTokenID() {
		return tokenID;
	}

	/**
	 * Sets the token id.
	 *
	 * @param tokenID the new token id
	 */
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userID the new user id
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Gets the issue.
	 *
	 * @return the issue
	 */
	public Date getIssue() {
		// return sdf.format(issue);
		return issue;
	}

	/**
	 * Sets the issue.
	 *
	 * @param issue the new issue
	 */
	public void setIssue(Date issue) {
		this.issue = issue;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public Role[] getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(Role[] role) {
		this.role = role;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [tokenID=" + tokenID + ", userID=" + userID
				+ ", issue=" + issue + ", role="
				+ Arrays.toString(role) + "]";
	}

}
