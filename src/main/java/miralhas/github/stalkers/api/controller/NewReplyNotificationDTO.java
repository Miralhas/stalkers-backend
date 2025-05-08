package miralhas.github.stalkers.api.controller;

import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.model.notification.enums.Type;

public record NewReplyNotificationDTO(
		Long id,
		Type type,
		String title,
		String description,
		String userReplying,
		String replyCommentContent,
		String parentCommentContent,
		String uri
) implements NotificationDTO {
}
