package kr.co.adflow.push.domain.bsbank;

/**
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
public class PollResponse {
	private int id;
	private int answerid;
	private int count;
	private String userid;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "PollResponse [id=" + id + ", answerid=" + answerid + ", count="
				+ count + ", userid=" + userid + ", content=" + content + "]";
	}

}
