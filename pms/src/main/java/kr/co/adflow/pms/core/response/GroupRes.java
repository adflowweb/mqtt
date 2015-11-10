package kr.co.adflow.pms.core.response;

public class GroupRes {

	private int groupCode;
	private String groupName;

	public int getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "GroupRes [groupCode=" + groupCode + ", groupName=" + groupName
				+ "]";
	}

}
