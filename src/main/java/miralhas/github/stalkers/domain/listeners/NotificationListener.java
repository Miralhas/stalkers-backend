package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.api.dto.input.NewChapterNotificationInput;
import miralhas.github.stalkers.api.dto.input.NewReplyNotificationInput;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.NewReplyNotification;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.NotificationService;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class NotificationListener {

	private final NotificationService notificationService;
	private final NovelRepository novelRepository;
	private final UserService userService;
	private final NovelService novelService;
	private final ChapterService chapterService;

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.notification.new-chapter"),
			key = "rk.notification.new-chapter"
	))
	public void onNewChapterUploaded(NewChapterNotificationInput input) {
		var recipients = novelRepository.findAllBookmarkedUsersOfANovel(input.novel().id());
		if (ObjectUtils.isEmpty(recipients)) return; // no one to send the notification to.

		var novel = input.novel();
		var chapter = input.chapter();
		var recipientsEmail = recipients.stream().map(User::getEmail).toList();
		var capitalizedNovelTitle = StringUtils.capitalize(novel.title());

		log.info("Sending new '{}' chapter notification to users: {}",
				novel.slug(), recipientsEmail);

		var notification = NewChapterNotification.builder()
				.title("New Chapter Released - %s".formatted(capitalizedNovelTitle))
				.description("Chapter %d is now live. Catch up on the latest chapters of %s".formatted(
						chapter.number(),
						capitalizedNovelTitle
				))
				.recipients(recipients)
				.novelSlug(novel.slug())
				.newChapterSlug(chapter.slug())
				.newChapterReleaseDate(chapter.createdAt())
				.build();

		notification = notificationService.saveNewChapterNotification(notification);
		log.info("Notification of id '{}' saved successfully!", notification.getId());
	}

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.notification.new-reply"),
			key = "rk.notification.new-reply"
	))
	public void onNewReplyMade(NewReplyNotificationInput newReply) {
		var parentComment = newReply.parentComment();
		var childComment = newReply.childComment();

		var commentOwner = userService.findUserByEmailOrException(parentComment.commenter());
		var userReplying = userService.findUserByEmailOrException(childComment.commenter());

		String uri = getURI(parentComment);

		log.info("Sending new reply notification to user: {}", parentComment.commenter());

		var notification = NewReplyNotification.builder()
				.userReplying(userReplying.getEmail())
				.recipients(Set.of(commentOwner))
				.title("Someone replied to your comment")
				.description("You’ve got a new reply on your comment. Join the conversation!")
				.parentCommentContent(newReply.parentComment().message())
				.replyCommentContent(newReply.childComment().message())
				.uri(uri)
				.build();


		notification = notificationService.saveNewReplyNotification(notification);
		log.info("Notifcation of id '{}' was sent successfully!", notification.getId());
	}

	private String getURI(UserCommentDTO parentComment) {
		String uri = "/novels/";
		if (parentComment.isNovelReview()) {
			return uri.concat(parentComment.slug());
		}

		var chapter = chapterService.findChapterBySlug(parentComment.slug());
		uri = uri.concat(chapter.getNovel().getSlug()).concat("/chapters/").concat(chapter.getSlug());
		return uri;
	}
}
