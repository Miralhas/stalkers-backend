package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

public record ChapterSummaryDTO(
		Long id,
		String title,
		String slug,
		Long number,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}

