package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.controller.NewReplyNotificationDTO;
import miralhas.github.stalkers.api.dto.NewChapterNotificationDTO;
import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.NewReplyNotification;
import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

	public NotificationDTO toResponse(Notification n) {
		if (n instanceof NewChapterNotification newChapterNotification) {
			return mapNewChapterNotification(newChapterNotification);
		} else if (n instanceof NewReplyNotification newReplyNotification) {
			return mapNewReplyNotification(newReplyNotification);
		} else {
			throw new InternalServerError("Unsupported Notification type: " + n.getClass().getName());
		}
	}

	private NewChapterNotificationDTO mapNewChapterNotification(NewChapterNotification n) {
		return new NewChapterNotificationDTO(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getNovelSlug(),
				n.getNewChapterSlug(),
				n.getNewChapterReleaseDate()
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
}
