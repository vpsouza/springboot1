package me.viniciuspiedade.springboot1.exceptions;

import java.util.List;

import me.viniciuspiedade.springboot1.util.ValidationResult;
import me.viniciuspiedade.springboot1.util.ValidationResult.Violation;

public class ErrorResponse {

	private int errorCode;
	private String message;
	private List<ValidationResult.Violation> violations;
	
	public ErrorResponse(int errorCode, String message, List<Violation> violations) {
		this.errorCode = errorCode;
		this.message = message;
		this.violations = violations;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
	public List<ValidationResult.Violation> getViolations() {
		return violations;
	}
	
}
