package miralhas.github.stalkers.api.dto;

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
		List<String> tags
) {
}
