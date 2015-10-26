package kr.co.adflow.pms.domain;

public class UserInfo {
	private String tokenId;
	private String deviceId;
	private String userName;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserInfo [tokenId=" + tokenId + ", deviceId=" + deviceId
				+ ", userName=" + userName + "]";
	}

}
