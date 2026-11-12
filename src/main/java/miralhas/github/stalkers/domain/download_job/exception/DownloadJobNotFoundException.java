package miralhas.github.stalkers.domain.download_job.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class DownloadJobNotFoundException extends ResourceNotFoundException {
	public DownloadJobNotFoundException(String message) {
		super(message);
	}

	public DownloadJobNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
