package miralhas.github.stalkers.domain.exception;

public class ChapterErrorNotFoundException extends ResourceNotFoundException {
	public ChapterErrorNotFoundException(String message) {
		super(message);
	}

	public ChapterErrorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
