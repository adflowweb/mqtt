/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 *
 * @author nadir93
 * @date 2014. 4. 22.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

	/** The user id. */
	private String userID;
	
	/** The name. */
	private String name;
	
	/** The title. */
	private String title;
	
	/** The dept. */
	private String dept;
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The password. */
	private String password;
	
	/** The device id. */
	private String deviceID;
	
	/** The apns token. */
	private String apnsToken;
	
	/** The role. */
	private String role;
	
	/** The ufmi. */
	private String ufmi;

	/**
	 * Instantiates a new user.
	 */
	public User() {
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the dept.
	 *
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}

	/**
	 * Sets the dept.
	 *
	 * @param dept the new dept
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone.
	 *
	 * @param phone the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceID() {
		return deviceID;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceID the new device id
	 */
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	/**
	 * Gets the apns token.
	 *
	 * @return the apns token
	 */
	public String getApnsToken() {
		return apnsToken;
	}

	/**
	 * Sets the apns token.
	 *
	 * @param apnsToken the new apns token
	 */
	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userID=" + userID + ", name=" + name + ", title=" + title
				+ ", dept=" + dept + ", email=" + email + ", phone=" + phone
				+ ", password=" + password + ", deviceID=" + deviceID
				+ ", apnsToken=" + apnsToken + ", role=" + role + "]";
	}

	/**
	 * Gets the ufmi.
	 *
	 * @return the ufmi
	 */
	public String getUfmi() {
		return ufmi;
	}

	/**
	 * Sets the ufmi.
	 *
	 * @param ufmi the new ufmi
	 */
	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

}
