package miralhas.github.stalkers.domain.exception;

public class ImageNotFoundException extends ResourceNotFoundException {

	public ImageNotFoundException(String message) {
		super(message);
	}

	public ImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
