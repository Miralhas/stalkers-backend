package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record NovelDTO(
		Long id,
		String slug,
		String title,
		String author,
		String status,
		String description,
		long chaptersCount,
		List<String> genres,
		List<String> tags,
		ChapterSummaryDTO firstChapter,
		ChapterSummaryDTO lastChapter

) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}