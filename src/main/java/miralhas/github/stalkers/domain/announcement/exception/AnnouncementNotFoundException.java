package miralhas.github.stalkers.domain.announcement.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class AnnouncementNotFoundException extends ResourceNotFoundException {
	public AnnouncementNotFoundException(String message) {
		super(message);
	}

	public AnnouncementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
