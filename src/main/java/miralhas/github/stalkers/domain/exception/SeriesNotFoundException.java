package miralhas.github.stalkers.domain.exception;

public class SeriesNotFoundException extends ResourceNotFoundException {
	public SeriesNotFoundException(String message) {
		super(message);
	}

	public SeriesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
