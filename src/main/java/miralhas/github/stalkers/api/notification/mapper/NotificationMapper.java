package miralhas.github.stalkers.api.notification.mapper;

import miralhas.github.stalkers.api.comment.dto.NewReplyNotification;
import miralhas.github.stalkers.api.notification.dto.NewChapterNotification;
import miralhas.github.stalkers.api.notification.dto.UpvoteNotification;
import miralhas.github.stalkers.domain.notification.interfaces.Notification;
import miralhas.github.stalkers.domain.shared.exception.InternalServerError;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotificationMapper {

	public Notification toResponse(miralhas.github.stalkers.domain.notification.model.Notification n) {
		return switch (n) {
			case miralhas.github.stalkers.domain.notification.model.NewChapterNotification newChapterNotification -> mapNewChapterNotification(newChapterNotification);
			case miralhas.github.stalkers.domain.notification.model.NewReplyNotification newReplyNotification -> mapNewReplyNotification(newReplyNotification);
			case miralhas.github.stalkers.domain.notification.model.UpvoteNotification upvoteNotification -> mapUpvoteNotification(upvoteNotification);
			case null, default ->
					throw new InternalServerError("Unsupported Notification type: " + Objects.requireNonNull(n).getClass().getName());
		};
	}

	private NewChapterNotification mapNewChapterNotification(miralhas.github.stalkers.domain.notification.model.NewChapterNotification n) {
		return new NewChapterNotification(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getNovelSlug(),
				n.getNewChapterSlug()
		);
	}

	private NewReplyNotification mapNewReplyNotification(miralhas.github.stalkers.domain.notification.model.NewReplyNotification n) {
		return new NewReplyNotification(
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

	private UpvoteNotification mapUpvoteNotification(miralhas.github.stalkers.domain.notification.model.UpvoteNotification n) {
		return new UpvoteNotification(
				n.getId(),
				n.getType(),
				n.getCreatedAt(),
				n.getTitle(),
				n.getDescription(),
				n.getUri()
		);
	}
}
