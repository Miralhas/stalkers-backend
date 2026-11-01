package miralhas.github.stalkers.domain.novel.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class GenreNotFoundException extends ResourceNotFoundException {
	public GenreNotFoundException(String message) {
		super(message);
	}

	public GenreNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
