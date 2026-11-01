package miralhas.github.stalkers.domain.announcement.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class AnnouncementAlreadyExistsException extends BusinessException {

	public AnnouncementAlreadyExistsException(String message) {
		super(message);
	}

	public AnnouncementAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
