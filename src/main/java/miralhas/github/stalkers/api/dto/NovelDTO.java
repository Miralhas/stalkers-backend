package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public record NovelDTO(
		Long id,
		String slug,
		String title,
		String author,
		String status,
		String alias,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		Boolean isHidden,
		String description,
		long chaptersCount,
		MetricsDTO metrics,
		List<String> genres,
		List<String> tags,
		ChapterSummaryDTO firstChapter,
		ChapterSummaryDTO lastChapter
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}