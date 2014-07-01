package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 7. 1.
 */
public class Group {

	private String groupID;
	private String name;

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Group [groupID=" + groupID + ", name=" + name + "]";
	}

}
