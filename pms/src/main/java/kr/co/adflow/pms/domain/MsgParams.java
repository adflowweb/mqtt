package kr.co.adflow.pms.domain;

public class MsgParams {
	
	private String keyMon;
	private String issueId;
	private int iDisplayStart;
	private int iDisplayLength;
	
	public String getKeyMon() {
		return keyMon;
	}
	public void setKeyMon(String keyMon) {
		this.keyMon = keyMon;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

}
