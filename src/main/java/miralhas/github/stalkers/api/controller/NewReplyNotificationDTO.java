package miralhas.github.stalkers.api.controller;

import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;

import java.time.OffsetDateTime;

public record NewReplyNotificationDTO(
		Long id,
		NotificationType type,
		OffsetDateTime createdAt,
		String title,
		String description,
		String userReplying,
		String replyCommentContent,
		String parentCommentContent,
		String uri
) implements NotificationDTO {
}
