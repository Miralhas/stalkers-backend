package miralhas.github.stalkers.api.notification.dto;

import miralhas.github.stalkers.domain.notification.interfaces.Notification;
import miralhas.github.stalkers.domain.notification.model.enums.NotificationType;

import java.time.OffsetDateTime;

public record NewChapterNotification(
		Long id,
		NotificationType type,
		OffsetDateTime createdAt,
		String title,
		String description,
		String novelSlug,
		String chapterSlug
) implements Notification {
}
