package kr.co.adflow.push.domain.bsbank;

import java.util.Arrays;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 설문조사
 * 
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Poll {

	private int id;
	private String title;
	private Date start;
	private Date end;
	private int responses;
	private int status;
	private String[] answers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getResponses() {
		return responses;
	}

	public void setResponses(int responses) {
		this.responses = responses;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "Poll [id=" + id + ", title=" + title + ", start=" + start
				+ ", end=" + end + ", responses=" + responses + ", status="
				+ status + ", answers=" + Arrays.toString(answers) + "]";
	}

}
