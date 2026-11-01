package miralhas.github.stalkers.domain.novel.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class TagNotFoundException extends ResourceNotFoundException {
	public TagNotFoundException(String message) {
		super(message);
	}

	public TagNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
