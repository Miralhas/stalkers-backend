package miralhas.github.stalkers.domain.exception;

public class RatingNotFoundException extends ResourceNotFoundException {
	public RatingNotFoundException(String message) {
		super(message);
	}

	public RatingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
