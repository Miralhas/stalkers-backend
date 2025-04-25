package miralhas.github.stalkers.api.dto;

public record ChapterDTO(
		Long id,
		String title,
		String slug,
		String body
) {
}
