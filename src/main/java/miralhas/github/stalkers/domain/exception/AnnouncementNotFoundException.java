package miralhas.github.stalkers.domain.exception;

public class AnnouncementNotFoundException extends ResourceNotFoundException {
	public AnnouncementNotFoundException(String message) {
		super(message);
	}

	public AnnouncementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
