package kr.co.adflow.pms.core.request;

public class RoleReq {

	private int groupCode;
	private int apiCode;
	private String apiUrl;
	private String apiMethod;
	private String apiName;

	public int getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}

	public int getApiCode() {
		return apiCode;
	}

	public void setApiCode(int apiCode) {
		this.apiCode = apiCode;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiMethod() {
		return apiMethod;
	}

	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	@Override
	public String toString() {
		return "RoleReq [groupCode=" + groupCode + ", apiCode=" + apiCode
				+ ", apiUrl=" + apiUrl + ", apiMethod=" + apiMethod
				+ ", apiName=" + apiName + "]";
	}

}
