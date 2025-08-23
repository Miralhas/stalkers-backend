package miralhas.github.stalkers.domain.exception;

public class NovelAlreadySelectedException extends BusinessException {

	public NovelAlreadySelectedException(String message) {
		super(message);
	}

	public NovelAlreadySelectedException(String message, Throwable cause) {
		super(message, cause);
	}

}
