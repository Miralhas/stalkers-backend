package miralhas.github.stalkers.domain.download_job.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.download_job.dto.DownloadJobDTO;
import miralhas.github.stalkers.api.download_job.dto.input.DownloadJobInput;
import miralhas.github.stalkers.api.download_job.mapper.DownloadJobMapper;
import miralhas.github.stalkers.domain.download_job.exception.DownloadJobNotFoundException;
import miralhas.github.stalkers.domain.download_job.model.DownloadJob;
import miralhas.github.stalkers.domain.download_job.repository.DownloadJobRepository;
import miralhas.github.stalkers.domain.download_job.validation.DownloadJobValidator;
import miralhas.github.stalkers.domain.shared.event.SendTransactionalMessageEvent;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DownloadJobService {

	@Value("${stalkers.storage.local.dir.download}")
	private Path downloadStoragePath;

	private final DownloadJobMapper downloadJobMapper;
	private final DownloadJobRepository downloadJobRepository;
	private final ApplicationEventPublisher events;
	private final ErrorMessages errorMessages;

	public DownloadJob findByIdOrException(Long id) {
		return downloadJobRepository.findById(id).orElseThrow(() -> new DownloadJobNotFoundException(
				errorMessages.get("download.job.id.notFound", id)
		));
	}

	public StreamingResponseBody downloadEpub(DownloadJob job) {
		File file = validateJob(job);
		return outputStream -> {
			try (InputStream inputStream = new FileInputStream(file)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.flush();
			}
		};
	}

	@Transactional
	public DownloadJobDTO createDownloadJob(DownloadJobInput input) {
		var job = downloadJobRepository.save(downloadJobMapper.fromInput(input));
		DownloadJobDTO jobDto = downloadJobMapper.toResponse(job);
		var message = new SendTransactionalMessageEvent(
				jobDto.id().toString(), "rk.novel.downloader", "stalkers"
		);
		events.publishEvent(message);
		return jobDto;
	}

	private File validateJob(DownloadJob job) {
		DownloadJobValidator.validateStatus(job, errorMessages);
		return DownloadJobValidator.validateFile(downloadStoragePath.resolve(job.getFilePath()), errorMessages);
	}
}


