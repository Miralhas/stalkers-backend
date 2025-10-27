package miralhas.github.stalkers.domain.exception;

public class ChapterErrorAlreadyExistsException extends BusinessException {

	public ChapterErrorAlreadyExistsException(String message) {
		super(message);
	}

	public ChapterErrorAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
