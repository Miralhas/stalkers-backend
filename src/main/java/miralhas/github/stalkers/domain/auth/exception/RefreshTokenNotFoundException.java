package miralhas.github.stalkers.domain.auth.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class RefreshTokenNotFoundException extends ResourceNotFoundException {

	public RefreshTokenNotFoundException(String message) {
		super(message);
	}

	public RefreshTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
