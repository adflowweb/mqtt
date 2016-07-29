/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthReq.
 */
public class AuthReq {

	/** The user id. */
	@NotEmpty
	private String userId;

	/** The password. */
	@NotEmpty
	private String password;
	
	/** The user type. */
	private String type;
	
	

	/**
	 * Gets the user type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the user type.
	 *
	 * @param type the new user type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}