package miralhas.github.stalkers.domain.exception;

public class NovelNotFoundException extends ResourceNotFoundException {
	public NovelNotFoundException(String message) {
		super(message);
	}

	public NovelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
