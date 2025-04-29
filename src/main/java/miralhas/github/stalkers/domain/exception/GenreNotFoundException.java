package miralhas.github.stalkers.domain.exception;

public class GenreNotFoundException extends ResourceNotFoundException {
	public GenreNotFoundException(String message) {
		super(message);
	}

	public GenreNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
