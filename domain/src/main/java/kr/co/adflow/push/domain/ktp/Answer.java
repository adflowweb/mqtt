package kr.co.adflow.push.domain.ktp;

/**
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
public class Answer {
	// +----------+------------------+------+-----+---------+-------+
	// | Field | Type | Null | Key | Default | Extra |
	// +----------+------------------+------+-----+---------+-------+
	// | id | int(10) unsigned | NO | | NULL | |
	// | answerid | int(10) unsigned | YES | | NULL | |
	// | content | varchar(500) | YES | | NULL | |
	// +----------+------------------+------+-----+---------+-------+

	private int id;
	private int answerid;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnswerid() {
		return answerid;
	}

	public void setAnswerid(int answerid) {
		this.answerid = answerid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", answerid=" + answerid + ", content="
				+ content + "]";
	}

}
