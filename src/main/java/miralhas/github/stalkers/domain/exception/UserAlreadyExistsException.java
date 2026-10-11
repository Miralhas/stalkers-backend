package miralhas.github.stalkers.domain.exception;

import java.util.Map;

public class UserAlreadyExistsException extends BusinessException {

	public UserAlreadyExistsException(String message, Map<String, String> errors) {
		super(message, errors);
	}

	public UserAlreadyExistsException(String message) {
		super(message);
	}

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExistsException(String message, Throwable cause, Map<String, String> errors) {
		super(message, cause, errors);
	}

}