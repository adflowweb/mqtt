package kr.co.adflow.pms.adm.request;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswordReq {
	
	@NotEmpty
	private String oldPassword;
	@NotEmpty
	private String newPassword;
	@NotEmpty
	private String rePassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getRePassword() {
		return rePassword;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

}
