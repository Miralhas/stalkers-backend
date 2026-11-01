package miralhas.github.stalkers.domain.novel.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class NovelNotFoundException extends ResourceNotFoundException {
	public NovelNotFoundException(String message) {
		super(message);
	}

	public NovelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
