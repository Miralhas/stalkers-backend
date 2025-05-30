package miralhas.github.stalkers.domain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.NewChapterNotificationInput;
import miralhas.github.stalkers.api.dto.input.NewReplyNotificationInput;
import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.api.dto_mapper.CommentMapper;
import miralhas.github.stalkers.api.dto_mapper.NotificationMapper;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.event.SendMessageEvent;
import miralhas.github.stalkers.domain.exception.NotificationNotFoundException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.notification.Notification;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NewChapterNotificationRepository;
import miralhas.github.stalkers.domain.repository.NotificationRecipientRepository;
import miralhas.github.stalkers.domain.repository.NotificationRepository;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
	private final NovelMapper novelMapper;
	private final ChapterMapper chapterMapper;
	private final CommentMapper commentMapper;
	private final NotificationRecipientRepository notificationRecipientRepository;

	@PersistenceContext
	private final EntityManager entityManager;
	private final ValidateAuthorization validateAuthorization;

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
	public Notification saveNotification(Notification notification, Set<Long> recipientIds) {
		var recipients = userRepository.findAllById(recipientIds);
		recipients = recipients.stream().map(entityManager::merge).toList();
		recipients.forEach(notification::addRecipient);
		return newChapterRepository.save(notification);
	}

	public void sendNewChapterNotification(Novel novel, Chapter chapter) {
		var novelSummaryDTO = novelMapper.toSummaryResponse(novel);
		var chapterSummaryDTO = chapterMapper.toSummaryResponse(chapter);
		var newChapterNotificationInput = new NewChapterNotificationInput(chapterSummaryDTO, novelSummaryDTO);
		events.publishEvent(new SendMessageEvent(
				newChapterNotificationInput, "rk.notification.new-chapter", "stalkers")
		);
	}

	// will send a notification to the parent of the comment, informing him of a new reply.
	public void sendCommentReplyNotification(Comment comment) {
		if (comment.hasParent()) {
			if (comment.getCommenter().equals(comment.getParentComment().getCommenter())) return; // same user replying to its own comment
			var childCommentDTO = commentMapper.toUserCommentDTO(comment);
			var parentCommentDTO = commentMapper.toUserCommentDTO(comment.getParentComment());
			var newReplyNotificationInput = new NewReplyNotificationInput(parentCommentDTO, childCommentDTO);
			events.publishEvent(new SendMessageEvent(
					newReplyNotificationInput, "rk.notification.new-reply", "stalkers")
			);
		}
	}

	public void sendUpvoteNotification(Comment comment) {
		var userCommentDTO = commentMapper.toUserCommentDTO(comment);
		events.publishEvent(new SendMessageEvent(
				userCommentDTO, "rk.notification.upvote", "stalkers")
		);
	}

	@Transactional
	public void readAllUserUnreadNotifications() {
		var user = validateAuthorization.getCurrentUser();
		notificationRecipientRepository.updateAllUserUnreadNotifications(user.getId());
	}

	@Transactional
	public void removeUserFromRecipients(User user, Long notificationId) {
		var notification = findNotificationByIdOrException(notificationId);
		notification.removeRecipient(user);
		notificationRepository.save(notification);
	}


}
