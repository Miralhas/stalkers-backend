package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.domain.model.notification.enums.Type;

import java.time.OffsetDateTime;

public record NotificationDTO(
		Long id,
		Type type,
		String title,
		String description,
		String novelSlug,
		String chapterSlug,
		OffsetDateTime newChapterReleaseDate
) {
}
