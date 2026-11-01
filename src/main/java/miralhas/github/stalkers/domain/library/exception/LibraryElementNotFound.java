package miralhas.github.stalkers.domain.library.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class LibraryElementNotFound extends ResourceNotFoundException {
	public LibraryElementNotFound(String message) {
		super(message);
	}

	public LibraryElementNotFound(String message, Throwable cause) {
		super(message, cause);
	}
}
