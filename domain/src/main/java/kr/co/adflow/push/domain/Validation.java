package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 4. 23.
 * 
 */
public class Validation {
	private boolean validation;

	public Validation(boolean validation) {
		this.validation = validation;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	@Override
	public String toString() {
		return "Validation [validation=" + validation + "]";
	}

}
