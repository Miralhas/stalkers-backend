package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.api.dto.input.NewChapterNotificationInput;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class NotificationListener {

	private final NotificationService notificationService;
	private final NovelRepository novelRepository;

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
}
