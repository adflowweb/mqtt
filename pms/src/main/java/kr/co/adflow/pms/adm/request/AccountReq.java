package kr.co.adflow.pms.adm.request;

public class AccountReq {
	
	private String userName;
	private String ipFilters;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpFilters() {
		return ipFilters;
	}
	public void setIpFilters(String ipFilters) {
		this.ipFilters = ipFilters;
	}

}
