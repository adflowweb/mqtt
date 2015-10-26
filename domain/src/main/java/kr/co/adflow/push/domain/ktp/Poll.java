/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

import java.util.Arrays;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * 설문조사.
 *
 * @author nadir93
 * @date 2014. 7. 10.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Poll {

	/** The id. */
	private int id;
	
	/** The title. */
	private String title;
	
	/** The start. */
	private Date start;
	
	/** The end. */
	private Date end;
	
	/** The responses. */
	private int responses;
	
	/** The status. */
	private int status;
	
	/** The anonymous. */
	private int anonymous;
	
	/** The answers. */
	private String[] answers;
	
	/** The result. */
	private float[] result;

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
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 *
	 * @param start the new start
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Sets the end.
	 *
	 * @param end the new end
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * Gets the responses.
	 *
	 * @return the responses
	 */
	public int getResponses() {
		return responses;
	}

	/**
	 * Sets the responses.
	 *
	 * @param responses the new responses
	 */
	public void setResponses(int responses) {
		this.responses = responses;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the anonymous.
	 *
	 * @return the anonymous
	 */
	public int getAnonymous() {
		return anonymous;
	}

	/**
	 * Sets the anonymous.
	 *
	 * @param anonymous the new anonymous
	 */
	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}

	/**
	 * Gets the answers.
	 *
	 * @return the answers
	 */
	public String[] getAnswers() {
		return answers;
	}

	/**
	 * Sets the answers.
	 *
	 * @param answers the new answers
	 */
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public float[] getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(float[] result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Poll [id=" + id + ", title=" + title + ", start=" + start
				+ ", end=" + end + ", responses=" + responses + ", status="
				+ status + ", anonymous=" + anonymous + ", answers="
				+ Arrays.toString(answers) + ", result="
				+ Arrays.toString(result) + "]";
	}

}
