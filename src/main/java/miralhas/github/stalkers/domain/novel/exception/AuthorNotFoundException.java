package miralhas.github.stalkers.domain.novel.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class AuthorNotFoundException extends ResourceNotFoundException {
	public AuthorNotFoundException(String message) {
		super(message);
	}

	public AuthorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
