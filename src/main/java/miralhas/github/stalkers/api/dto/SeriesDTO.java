package miralhas.github.stalkers.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record SeriesDTO(
		String title,
		String slug,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		List<NovelSummaryDTO> books
) {
}
