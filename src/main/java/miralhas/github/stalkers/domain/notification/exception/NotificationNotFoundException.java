package miralhas.github.stalkers.domain.notification.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class NotificationNotFoundException extends ResourceNotFoundException {
	public NotificationNotFoundException(String message) {
		super(message);
	}

	public NotificationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
