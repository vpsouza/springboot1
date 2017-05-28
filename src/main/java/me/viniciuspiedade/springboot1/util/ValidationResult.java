package me.viniciuspiedade.springboot1.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ValidationResult {
	static ValidationResult valid() {
		return ValidationSupport.valid();
	}

	static ValidationResult invalid(List<Violation> violations) {
		return new Invalid(Optional.of(violations));
	}
	
	static ValidationResult invalid(Violation violation) {
		List<Violation> violations = new ArrayList<Violation>();
		violations.add(violation);
		return new Invalid(Optional.of(violations));
	}

	boolean isValid();

	Optional<List<Violation>> getViolations();
	
	Optional<String> getStrViolations();

	final static class ValidationSupport {

		private static final ValidationResult valid = new ValidationResult() {
			public boolean isValid() {
				return true;
			}

			public Optional<List<Violation>> getViolations() {
				return Optional.empty();
			}

			@Override
			public Optional<String> getStrViolations() {
				return Optional.of("");
			}
		};

		static ValidationResult valid() {
			return valid;
		}
	}

	final static class Invalid implements ValidationResult {

		private final List<Violation> violations;

		public Invalid(Optional<List<Violation>> violations) {
			this.violations = new ArrayList<Violation>(violations.orElse(new ArrayList<Violation>()));
		}

		// Instead of getReason
		public Optional<List<Violation>> getViolations() {
			return Optional.of(Collections.unmodifiableList(violations));
		}

		public boolean isValid() {
			return false;
		}

		@Override
		public Optional<String> getStrViolations() {
			return Optional.of(violations.stream().map(v -> v.getErrorMessage())
		            .collect(Collectors.joining("\n")));
		}
	}

	// this class captures the reason why something has failed
	// it has a written errorMessage and a specific ErrorEnum error to indicate
	// the failure
	// For example, for i18n the errorEnum might be needed to identify the right
	// message
	public final static class Violation {
		private final String errorMessage;
		private ErrorEnum error;

		private Violation(String errorMessage, ErrorEnum error) {
			this.errorMessage = errorMessage;
			this.error = error;
		}

		public static Violation violation(String errorMessage, ErrorEnum error) {
			return new Violation(errorMessage, error);
		}
		
		public String getErrorMessage() {
			return this.errorMessage;
		}
		
		public ErrorEnum getError() {
			return this.error;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((error == null) ? 0 : error.hashCode());
			result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Violation other = (Violation) obj;
			if (error != other.error)
				return false;
			if (errorMessage == null) {
				if (other.errorMessage != null)
					return false;
			} else if (!errorMessage.equals(other.errorMessage))
				return false;
			return true;
		}

	}
}
