package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record ChapterDTO(
		Long id,
		String title,
		String slug,
		String body,
		String novelSlug,
		String novelTitle,
		String novelStatus,
		Long novelId,
		Long novelChaptersCount,
		Long number,
		ChapterSummaryDTO previous,
		ChapterSummaryDTO next
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
