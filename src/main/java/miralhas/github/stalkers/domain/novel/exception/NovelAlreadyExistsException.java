package miralhas.github.stalkers.domain.novel.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class NovelAlreadyExistsException extends BusinessException {

	public NovelAlreadyExistsException(String message) {
		super(message);
	}

	public NovelAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
