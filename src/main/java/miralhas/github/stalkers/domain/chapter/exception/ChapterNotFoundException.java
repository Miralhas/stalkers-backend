package miralhas.github.stalkers.domain.chapter.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class ChapterNotFoundException extends ResourceNotFoundException {
	public ChapterNotFoundException(String message) {
		super(message);
	}

	public ChapterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
