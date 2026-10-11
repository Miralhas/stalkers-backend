package miralhas.github.stalkers.domain.exception;

public class SeriesAlreadyExistsException extends ResourceAlreadyExistsException {

	public SeriesAlreadyExistsException(String message) {
		super(message);
	}

	public SeriesAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
