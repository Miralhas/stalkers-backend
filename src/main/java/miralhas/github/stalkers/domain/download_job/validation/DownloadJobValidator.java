package miralhas.github.stalkers.domain.download_job.validation;

import miralhas.github.stalkers.domain.download_job.exception.DownloadFileNotFoundException;
import miralhas.github.stalkers.domain.download_job.exception.UnavailableDownloadException;
import miralhas.github.stalkers.domain.download_job.model.DownloadJob;
import miralhas.github.stalkers.shared.utils.ErrorMessages;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class DownloadJobValidator {

	public static File validateFile(Path epubPath, ErrorMessages m) {
		var file = new File(epubPath.toUri());
		if (!file.exists()) throw new DownloadFileNotFoundException(m.get("download.job.file.not.exists"));
		return file;
	}

	public static void validateStatus(DownloadJob job, ErrorMessages m) {
		List<ValidationRule> rules = List.of(
				new ValidationRule(DownloadJob::pending, m.get("download.job.pending")),
				new ValidationRule(DownloadJob::failed, m.get("download.job.failed"))
		);

		for (ValidationRule rule : rules) {
			if (rule.predicate().test(job)) {
				throw new UnavailableDownloadException(rule.errorMessage());
			}
		}
	}

	private record ValidationRule(Predicate<DownloadJob> predicate, String errorMessage) {
	}
}
