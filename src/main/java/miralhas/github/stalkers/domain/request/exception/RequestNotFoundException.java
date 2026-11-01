package miralhas.github.stalkers.domain.request.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class RequestNotFoundException extends ResourceNotFoundException {
	public RequestNotFoundException(String message) {
		super(message);
	}

	public RequestNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
