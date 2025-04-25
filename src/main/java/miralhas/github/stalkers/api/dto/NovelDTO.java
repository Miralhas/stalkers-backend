package miralhas.github.stalkers.api.dto;

public record NovelDTO(
		Long id,
		String slug,
		String title,
		String author,
		String status,
		String description,
		long chaptersCount
) {
}
