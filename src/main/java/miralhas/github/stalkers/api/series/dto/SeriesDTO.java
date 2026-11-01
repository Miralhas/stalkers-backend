package miralhas.github.stalkers.api.series.dto;

import java.time.OffsetDateTime;

public record SeriesDTO(
		String title,
		String slug,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) {
}
