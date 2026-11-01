package miralhas.github.stalkers.api.comment.dto;

import miralhas.github.stalkers.domain.notification.interfaces.Notification;
import miralhas.github.stalkers.domain.notification.model.enums.NotificationType;

import java.time.OffsetDateTime;

public record NewReplyNotification(
		Long id,
		NotificationType type,
		OffsetDateTime createdAt,
		String title,
		String description,
		String userReplying,
		String replyCommentContent,
		String parentCommentContent,
		String uri
) implements Notification {
}
