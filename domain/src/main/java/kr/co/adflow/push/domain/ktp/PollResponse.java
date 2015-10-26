/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

// TODO: Auto-generated Javadoc
/**
 * The Class PollResponse.
 *
 * @author nadir93
 * @date 2014. 7. 10.
 */
public class PollResponse {
	
	/** The id. */
	private int id;
	
	/** The answerid. */
	private int answerid;
	
	/** The count. */
	private int count;
	
	/** The userid. */
	private String userid;
	
	/** The content. */
	private String content;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the answerid.
	 *
	 * @return the answerid
	 */
	public int getAnswerid() {
		return answerid;
	}

	/**
	 * Sets the answerid.
	 *
	 * @param answerid the new answerid
	 */
	public void setAnswerid(int answerid) {
		this.answerid = answerid;
	}

	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * Sets the userid.
	 *
	 * @param userid the new userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PollResponse [id=" + id + ", answerid=" + answerid + ", count="
				+ count + ", userid=" + userid + ", content=" + content + "]";
	}

}
