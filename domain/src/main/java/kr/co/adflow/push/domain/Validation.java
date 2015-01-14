/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Validation.
 *
 * @author nadir93
 * @date 2014. 4. 23.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Validation {
	
	/** The validation. */
	private boolean validation;

	/**
	 * Instantiates a new validation.
	 */
	public Validation() {
	}

	/**
	 * Instantiates a new validation.
	 *
	 * @param validation the validation
	 */
	public Validation(boolean validation) {
		this.validation = validation;
	}

	/**
	 * Checks if is validation.
	 *
	 * @return true, if is validation
	 */
	public boolean isValidation() {
		return validation;
	}

	/**
	 * Sets the validation.
	 *
	 * @param validation the new validation
	 */
	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Validation [validation=" + validation + "]";
	}

}
