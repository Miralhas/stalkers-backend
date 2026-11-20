package miralhas.github.stalkers.domain.user.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
