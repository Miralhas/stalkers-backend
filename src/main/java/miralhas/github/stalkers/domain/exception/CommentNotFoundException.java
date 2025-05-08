package miralhas.github.stalkers.domain.exception;

public class CommentNotFoundException extends ResourceNotFoundException {
	public CommentNotFoundException(String message) {
		super(message);
	}

	public CommentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
