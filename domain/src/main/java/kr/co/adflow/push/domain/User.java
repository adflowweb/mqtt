package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

	private String userID;
	private String name;
	private String title;
	private String dept;
	private String email;
	private String phone;
	private String password;
	private String deviceID;

	public User() {
	}

	public User(String userID, String name, String title, String dept,
			String email, String phone) {
		super();
		this.userID = userID;
		this.name = name;
		this.title = title;
		this.dept = dept;
		this.email = email;
		this.phone = phone;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", name=" + name + ", title=" + title
				+ ", dept=" + dept + ", email=" + email + ", phone=" + phone
				+ ", password=" + password + ", deviceID=" + deviceID + "]";
	}

}