package kr.co.adflow.pms.core.request;

public class GroupReq {

	private int groupCode;
	private String groupName;
	private String groupDescription;

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

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	@Override
	public String toString() {
		return "GroupReq [groupCode=" + groupCode + ", groupName=" + groupName
				+ ", groupDescription=" + groupDescription + "]";
	}

}
