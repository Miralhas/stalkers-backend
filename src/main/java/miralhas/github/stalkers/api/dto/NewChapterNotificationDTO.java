package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.model.notification.enums.Type;

import java.time.OffsetDateTime;

public record NewChapterNotificationDTO(
		Long id,
		Type type,
		String title,
		String description,
		String novelSlug,
		String chapterSlug,
		OffsetDateTime newChapterReleaseDate
) implements NotificationDTO {
}
