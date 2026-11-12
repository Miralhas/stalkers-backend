package miralhas.github.stalkers.api.download_job.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

public record DownloadJobDTO(
		Long id,
		String slug,
		Integer start,
		Integer end,
		String status,
		String path,
		String error,
		OffsetDateTime createdAt
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}

