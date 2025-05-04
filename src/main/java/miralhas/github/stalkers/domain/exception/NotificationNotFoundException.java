package miralhas.github.stalkers.domain.exception;

public class NotificationNotFoundException extends ResourceNotFoundException {
	public NotificationNotFoundException(String message) {
		super(message);
	}

	public NotificationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
