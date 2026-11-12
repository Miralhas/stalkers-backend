package miralhas.github.stalkers.domain.download_job.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

import java.util.Map;

public class DownloadFileNotFoundException extends ResourceNotFoundException {
	public DownloadFileNotFoundException(String message) {
		super(message);
	}

	public DownloadFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DownloadFileNotFoundException() {
		super();
	}

	public DownloadFileNotFoundException(String message, Map<String, String> errors) {
		super(message, errors);
	}

	public DownloadFileNotFoundException(String message, Throwable cause, Map<String, String> errors) {
		super(message, cause, errors);
	}

	public DownloadFileNotFoundException(Throwable cause) {
		super(cause);
	}

	public DownloadFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
