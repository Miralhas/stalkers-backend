package miralhas.github.stalkers.domain.request.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class ChapterErrorNotFoundException extends ResourceNotFoundException {
	public ChapterErrorNotFoundException(String message) {
		super(message);
	}

	public ChapterErrorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
