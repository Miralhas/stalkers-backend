package miralhas.github.stalkers.domain.exception;

import java.util.Map;

public abstract class ResourceNotFoundException extends BusinessException {

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, Map<String, String> errors) {
		super(message, errors);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message, Throwable cause, Map<String, String> errors) {
		super(message, cause, errors);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
