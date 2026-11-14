package miralhas.github.stalkers.api.download_job.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.download_job.dto.DownloadJobDTO;
import miralhas.github.stalkers.api.download_job.dto.input.DownloadJobInput;
import miralhas.github.stalkers.api.download_job.mapper.DownloadJobMapper;
import miralhas.github.stalkers.domain.download_job.service.DownloadJobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class DownloadJobController {
	@Value("${stalkers.storage.local.dir.download}")
	private Path downloadStoragePath;

	private final DownloadJobService downloadJobService;
	private final DownloadJobMapper downloadJobMapper;

	@GetMapping("/{id}/download")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
		var job = downloadJobService.findByIdOrException(id);
		var responseBody = downloadJobService.downloadEpub(job);

		var filename = job.getNormalizedFilename();
		var length = downloadStoragePath.resolve(job.getFilePath()).toFile().length();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(length));

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(responseBody);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public DownloadJobDTO getById(@PathVariable Long id) {
		return downloadJobMapper.toResponse(downloadJobService.findByIdOrException(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DownloadJobDTO create(@RequestBody @Valid DownloadJobInput input) {
		return downloadJobService.createDownloadJob(input);
	}
}
