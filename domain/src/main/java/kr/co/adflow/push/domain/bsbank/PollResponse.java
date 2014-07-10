package kr.co.adflow.push.domain.bsbank;

/**
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
public class PollResponse {
	private int id;
	private int answerid;
	private String userid;

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

	@Override
	public String toString() {
		return "PollResponse [id=" + id + ", answerid=" + answerid
				+ ", userid=" + userid + "]";
	}

}
