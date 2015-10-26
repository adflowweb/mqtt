/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

// TODO: Auto-generated Javadoc
/**
 * The Class Answer.
 *
 * @author nadir93
 * @date 2014. 7. 10.
 */
public class Answer {
	// +----------+------------------+------+-----+---------+-------+
	// | Field | Type | Null | Key | Default | Extra |
	// +----------+------------------+------+-----+---------+-------+
	// | id | int(10) unsigned | NO | | NULL | |
	// | answerid | int(10) unsigned | YES | | NULL | |
	// | content | varchar(500) | YES | | NULL | |
	// +----------+------------------+------+-----+---------+-------+

	/** The id. */
	private int id;
	
	/** The answerid. */
	private int answerid;
	
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
		return "Answer [id=" + id + ", answerid=" + answerid + ", content="
				+ content + "]";
	}

}
