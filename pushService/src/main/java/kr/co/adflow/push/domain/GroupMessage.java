package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class GroupMessage extends Message {
	private String groupName;

	/**
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "GroupMessage [groupName=" + groupName + ", toString()="
				+ super.toString() + "]";
	}

}
