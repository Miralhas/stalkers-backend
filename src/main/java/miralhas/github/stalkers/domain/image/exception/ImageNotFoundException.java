package miralhas.github.stalkers.domain.image.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class ImageNotFoundException extends ResourceNotFoundException {

	public ImageNotFoundException(String message) {
		super(message);
	}

	public ImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
