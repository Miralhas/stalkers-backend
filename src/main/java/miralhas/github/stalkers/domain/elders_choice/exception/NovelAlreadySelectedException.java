package miralhas.github.stalkers.domain.elders_choice.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class NovelAlreadySelectedException extends BusinessException {

	public NovelAlreadySelectedException(String message) {
		super(message);
	}

	public NovelAlreadySelectedException(String message, Throwable cause) {
		super(message, cause);
	}

}
