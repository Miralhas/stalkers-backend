package miralhas.github.stalkers.domain.exception;

public class AuthorNotFoundException extends ResourceNotFoundException {
	public AuthorNotFoundException(String message) {
		super(message);
	}

	public AuthorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
