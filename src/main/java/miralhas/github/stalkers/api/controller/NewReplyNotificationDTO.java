package miralhas.github.stalkers.api.controller;

import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;

public record NewReplyNotificationDTO(
		Long id,
		NotificationType type,
		String title,
		String description,
		String userReplying,
		String replyCommentContent,
		String parentCommentContent,
		String uri
) implements NotificationDTO {
}
