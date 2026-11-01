package miralhas.github.stalkers.domain.comment.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {
	public CommentNotFoundException(String message) {
		super(message);
	}

	public CommentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
