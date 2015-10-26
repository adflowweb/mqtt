/*
 * 
 */
package kr.co.adflow.push.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class Group.
 *
 * @author nadir93
 * @date 2014. 7. 1.
 */
public class Group {

	/** The group id. */
	private String groupID;
	
	/** The name. */
	private String name;

	/**
	 * Gets the group id.
	 *
	 * @return the group id
	 */
	public String getGroupID() {
		return groupID;
	}

	/**
	 * Sets the group id.
	 *
	 * @param groupID the new group id
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [groupID=" + groupID + ", name=" + name + "]";
	}

}
