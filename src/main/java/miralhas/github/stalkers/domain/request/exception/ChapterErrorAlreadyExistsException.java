package miralhas.github.stalkers.domain.request.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class ChapterErrorAlreadyExistsException extends BusinessException {

	public ChapterErrorAlreadyExistsException(String message) {
		super(message);
	}

	public ChapterErrorAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
