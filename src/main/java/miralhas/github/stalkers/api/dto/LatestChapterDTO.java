package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
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
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
