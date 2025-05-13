package miralhas.github.stalkers.domain.exception;

public class ChapterNotFoundException extends ResourceNotFoundException {
	public ChapterNotFoundException(String message) {
		super(message);
	}

	public ChapterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
