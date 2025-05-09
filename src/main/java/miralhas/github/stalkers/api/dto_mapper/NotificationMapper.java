package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.controller.NewReplyNotificationDTO;
import miralhas.github.stalkers.api.dto.NewChapterNotificationDTO;
import miralhas.github.stalkers.api.dto.UpvoteNotificationDTO;
import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.NewReplyNotification;
import miralhas.github.stalkers.domain.model.notification.Notification;
import miralhas.github.stalkers.domain.model.notification.UpvoteNotification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotificationMapper {

	public NotificationDTO toResponse(Notification n) {
		return switch (n) {
			case NewChapterNotification newChapterNotification -> mapNewChapterNotification(newChapterNotification);
			case NewReplyNotification newReplyNotification -> mapNewReplyNotification(newReplyNotification);
			case UpvoteNotification upvoteNotification -> mapUpvoteNotification(upvoteNotification);
			case null, default ->
					throw new InternalServerError("Unsupported Notification type: " + Objects.requireNonNull(n).getClass().getName());
		};
	}

	private NewChapterNotificationDTO mapNewChapterNotification(NewChapterNotification n) {
		return new NewChapterNotificationDTO(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getNovelSlug(),
				n.getNewChapterSlug()
		);
	}

	private NewReplyNotificationDTO mapNewReplyNotification(NewReplyNotification n) {
		return new NewReplyNotificationDTO(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getUserReplying(),
				n.getReplyCommentContent(),
				n.getParentCommentContent(),
				n.getUri()
		);
	}

	private UpvoteNotificationDTO mapUpvoteNotification(UpvoteNotification n) {
		return new UpvoteNotificationDTO(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getUri()
		);
	}
}
