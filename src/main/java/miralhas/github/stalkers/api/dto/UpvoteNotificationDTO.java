package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;

import java.time.OffsetDateTime;

public record UpvoteNotificationDTO(
		Long id,
		NotificationType type,
		OffsetDateTime createdAt,
		String title,
		String description,
		String uri
) implements NotificationDTO {
}
