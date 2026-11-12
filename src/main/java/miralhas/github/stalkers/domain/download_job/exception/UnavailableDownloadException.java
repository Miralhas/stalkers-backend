package miralhas.github.stalkers.domain.download_job.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class UnavailableDownloadException extends BusinessException {

	public UnavailableDownloadException(String message) {
		super(message);
	}

	public UnavailableDownloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
