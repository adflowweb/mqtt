/*
 * 
 */
package kr.co.adflow.pms.inf.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class PasswordReq.
 */
public class PasswordReq {

	/** The old password. */
	@NotEmpty
	private String oldPassword;
	
	/** The new password. */
	@NotEmpty
	private String newPassword;
	
	/** The re password. */
	@NotEmpty
	private String rePassword;

	/**
	 * Gets the old password.
	 *
	 * @return the old password
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * Sets the old password.
	 *
	 * @param oldPassword the new old password
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * Gets the new password.
	 *
	 * @return the new password
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * Sets the new password.
	 *
	 * @param newPassword the new new password
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * Gets the re password.
	 *
	 * @return the re password
	 */
	public String getRePassword() {
		return rePassword;
	}

	/**
	 * Sets the re password.
	 *
	 * @param rePassword the new re password
	 */
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

}
