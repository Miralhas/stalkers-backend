package miralhas.github.stalkers.domain.exception;

public class PasswordResetTokenNotFoundException extends ResourceNotFoundException {

	public PasswordResetTokenNotFoundException(String message) {
		super(message);
	}

	public PasswordResetTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
