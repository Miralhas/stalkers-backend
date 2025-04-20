package miralhas.github.stalkers.domain.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserAlreadyExistsException extends BusinessException {

	private final Map<String, String> errors;

	public UserAlreadyExistsException(String message, Map<String, String> errors) {
		super(message);
		this.errors = errors;
	}

	public UserAlreadyExistsException(String message, Map<String, String> errors, Throwable cause) {
		super(message, cause);
		this.errors = errors;
	}

}