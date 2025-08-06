package miralhas.github.stalkers.api.dto;

import java.time.OffsetDateTime;

public record LatestChapterDTO(
		Long id,
		Long novelId,
		String slug,
		Long chapterNumber,
		String title,
		String author,
		String novelTitle,
		String novelSlug,
		OffsetDateTime createdAt
) {
}
