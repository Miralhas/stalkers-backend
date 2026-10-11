package miralhas.github.stalkers.domain.exception;

import java.util.Map;

public abstract class ResourceAlreadyExistsException extends BusinessException {
	public ResourceAlreadyExistsException() {
	}

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}

	public ResourceAlreadyExistsException(String message, Map<String, String> errors) {
		super(message, errors);
	}

	public ResourceAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceAlreadyExistsException(String message, Throwable cause, Map<String, String> errors) {
		super(message, cause, errors);
	}

	public ResourceAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public ResourceAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
