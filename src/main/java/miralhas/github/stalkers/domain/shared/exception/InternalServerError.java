package miralhas.github.stalkers.domain.shared.exception;

public class InternalServerError extends RuntimeException {
	public InternalServerError(String message) {
		super(message);
	}

	public InternalServerError() {
	}
}
