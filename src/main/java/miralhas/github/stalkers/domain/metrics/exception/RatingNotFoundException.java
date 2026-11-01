package miralhas.github.stalkers.domain.metrics.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class RatingNotFoundException extends ResourceNotFoundException {
	public RatingNotFoundException(String message) {
		super(message);
	}

	public RatingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
