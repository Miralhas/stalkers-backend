package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.NotificationDTO;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

	public NotificationDTO toResponse(Notification n) {
		if (n instanceof NewChapterNotification newChapterNotification) {
			return mapNewChapterNotification(newChapterNotification);
		} else {
			throw new InternalServerError();
		}
	}

	private NotificationDTO mapNewChapterNotification(NewChapterNotification n) {
		return new NotificationDTO(
				n.getId(),
				n.getType(),
				n.getTitle(),
				n.getDescription(),
				n.getNovelSlug(),
				n.getNewChapterSlug(),
				n.getNewChapterReleaseDate()
		);
	}
}
