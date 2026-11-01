package miralhas.github.stalkers.domain.series.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceAlreadyExistsException;

import java.util.Map;

public class BookAlreadyBelongsException extends ResourceAlreadyExistsException {

	public BookAlreadyBelongsException() {
	}

	public BookAlreadyBelongsException(String message) {
		super(message);
	}

	public BookAlreadyBelongsException(String message, Map<String, String> errors) {
		super(message, errors);
	}

	public BookAlreadyBelongsException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookAlreadyBelongsException(String message, Throwable cause, Map<String, String> errors) {
		super(message, cause, errors);
	}

	public BookAlreadyBelongsException(Throwable cause) {
		super(cause);
	}

	public BookAlreadyBelongsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
