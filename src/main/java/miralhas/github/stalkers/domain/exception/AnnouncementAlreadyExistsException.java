package miralhas.github.stalkers.domain.exception;

public class AnnouncementAlreadyExistsException extends BusinessException {

	public AnnouncementAlreadyExistsException(String message) {
		super(message);
	}

	public AnnouncementAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
