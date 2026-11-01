package miralhas.github.stalkers.domain.auth.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class PasswordResetTokenNotFoundException extends ResourceNotFoundException {

	public PasswordResetTokenNotFoundException(String message) {
		super(message);
	}

	public PasswordResetTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
