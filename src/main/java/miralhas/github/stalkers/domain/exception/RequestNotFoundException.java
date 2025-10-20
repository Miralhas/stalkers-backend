package miralhas.github.stalkers.domain.exception;

public class RequestNotFoundException extends ResourceNotFoundException {
	public RequestNotFoundException(String message) {
		super(message);
	}

	public RequestNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
