package miralhas.github.stalkers.domain.exception;

public class TagNotFoundException extends ResourceNotFoundException {
	public TagNotFoundException(String message) {
		super(message);
	}

	public TagNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
