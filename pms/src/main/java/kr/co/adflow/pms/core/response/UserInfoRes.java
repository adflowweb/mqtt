package kr.co.adflow.pms.core.response;

import java.util.List;

import kr.co.adflow.pms.domain.UserInfo;

public class UserInfoRes {

	private List<UserInfo> userInfo;

	public List<UserInfo> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<UserInfo> userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "UserInfoRes [userInfo=" + userInfo + "]";
	}

}
