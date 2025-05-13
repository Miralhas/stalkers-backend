package miralhas.github.stalkers.domain.exception;

public class LibraryElementNotFound extends ResourceNotFoundException {
	public LibraryElementNotFound(String message) {
		super(message);
	}

	public LibraryElementNotFound(String message, Throwable cause) {
		super(message, cause);
	}
}
