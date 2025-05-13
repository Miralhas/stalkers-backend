package miralhas.github.stalkers.domain.exception;

public class NovelAlreadyExistsException extends BusinessException {

	public NovelAlreadyExistsException(String message) {
		super(message);
	}

	public NovelAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
