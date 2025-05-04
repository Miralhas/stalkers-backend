package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.NotificationDTO;
import miralhas.github.stalkers.api.dto.input.NewChapterNotificationInput;
import miralhas.github.stalkers.api.dto_mapper.NotificationMapper;
import miralhas.github.stalkers.domain.event.SendMessageEvent;
import miralhas.github.stalkers.domain.exception.NotificationNotFoundException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.Notification;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NewChapterNotificationRepository;
import miralhas.github.stalkers.domain.repository.NotificationRepository;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

	private final NewChapterNotificationRepository newChapterRepository;
	private final ApplicationEventPublisher events;
	private final NotificationRepository notificationRepository;
	private final ErrorMessages errorMessages;
	private final UserRepository userRepository;
	private final NotificationMapper notificationMapper;

	public Notification findNotificationByIdOrException(Long id) {
		return notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(
				errorMessages.get("notification.notFound.id", id)
		));
	}

	public List<NotificationDTO> getUserNotifications(User user) {
		return userRepository.findUserNotifications(user.getId()).stream()
				.map(notificationMapper::toResponse)
				.toList();
	}

	@Transactional
	public NewChapterNotification saveNewChapterNotification(NewChapterNotification notification) {
		return newChapterRepository.save(notification);
	}

	public void sendNewChapterNotification(Novel novel, Chapter chapter) {
		var newChapterNotificationInput = new NewChapterNotificationInput(chapter, novel);
		events.publishEvent(new SendMessageEvent(
				newChapterNotificationInput, "rk.notification.new-chapter", "stalkers")
		);
	}

	@Transactional
	public void removeUserFromRecipients(User user, Long notificationId) {
		var notification = findNotificationByIdOrException(notificationId);
		notification.getRecipients().remove(user);
		notificationRepository.save(notification);
	}

}
