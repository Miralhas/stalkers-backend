package miralhas.github.stalkers.domain.series.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class SeriesNotFoundException extends ResourceNotFoundException {
	public SeriesNotFoundException(String message) {
		super(message);
	}

	public SeriesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
