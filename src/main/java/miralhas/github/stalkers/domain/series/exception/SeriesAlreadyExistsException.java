package miralhas.github.stalkers.domain.series.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceAlreadyExistsException;

public class SeriesAlreadyExistsException extends ResourceAlreadyExistsException {

	public SeriesAlreadyExistsException(String message) {
		super(message);
	}

	public SeriesAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
