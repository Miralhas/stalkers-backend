package miralhas.github.stalkers.domain.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserAlreadyExistsException extends BusinessException {

	private final Map<String, String> errors;

	public UserAlreadyExistsException(Map<String, String> errors) {
		super();
		this.errors = errors;
	}

	public UserAlreadyExistsException(Map<String, String> errors, Throwable cause) {
		super(cause);
		this.errors = errors;
	}

}