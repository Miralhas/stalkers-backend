package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.api.dto.input.NewChapterNotificationInput;
import miralhas.github.stalkers.api.dto.input.NewReplyNotificationInput;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import miralhas.github.stalkers.domain.model.notification.NewChapterNotification;
import miralhas.github.stalkers.domain.model.notification.NewReplyNotification;
import miralhas.github.stalkers.domain.model.notification.UpvoteNotification;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.NotificationService;
import miralhas.github.stalkers.domain.service.ReviewService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
	private final ChapterService chapterService;
	private final ReviewService reviewService;

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.notification.new-chapter"),
			key = "rk.notification.new-chapter"
	))
	public void onNewChapterUploaded(NewChapterNotificationInput input) {
		var recipientIds = novelRepository.findAllBookmarkedUsersIdOfANovel(input.novel().id());
		if (ObjectUtils.isEmpty(recipientIds)) return; // no one to send the notification to.

		var novel = input.novel();
		var chapter = input.chapter();
		var capitalizedNovelTitle = StringUtils.capitalize(novel.title());

		log.info("Sending new '{}' chapter notification to users: {}",
				novel.slug(), recipientIds);

		var notification = new NewChapterNotification();
		notification.setType();
		notification.setTitle("New Chapter Released - %s".formatted(capitalizedNovelTitle));
		notification.setDescription("Chapter %d is now live. Catch up on the latest chapters of %s".formatted(
				chapter.number(),
				capitalizedNovelTitle
		));
		notification.setNovelSlug(novel.slug());
		notification.setNewChapterSlug(chapter.slug());

		var saved = notificationService.saveNotification(notification, recipientIds);
		log.info("Notification of id '{}' saved successfully!", saved.getId());
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

		var notification = new NewReplyNotification();
		notification.setType();
		notification.setUserReplying(userReplying.getEmail());
		notification.setTitle("%s replied to your comment".formatted(userReplying.getUsername()));
		notification.setDescription("You’ve got a new reply on your comment.");
		notification.setParentCommentContent(newReply.parentComment().message());
		notification.setReplyCommentContent(newReply.childComment().message());
		notification.setUri(uri);


		var saved = notificationService.saveNotification(notification, Set.of(commentOwner.getId()));
		log.info("New Reply Notification of id '{}' was sent successfully!", saved.getId());
	}

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.notification.upvote"),
			key = "rk.notification.upvote"
	))
	@Transactional
	public void onUpvoteThresholdReached(UserCommentDTO userComment) {
		var comment = reviewService.findCommentByIdOrThrowException(userComment.id());

		if (!validateUpvoteNotification(comment.getVoteCount())) return;

		log.info("Sending new Upvote Notification to user: {}", comment.getCommenter().getEmail());
		var notification = new UpvoteNotification();
		notification.setType();
		notification.setTitle("Your comment got upvoted!");
		notification.setDescription(getUpvoteDescription(comment));
		notification.setUri(getURI(userComment));

		var saved = notificationService.saveNotification(notification, Set.of(comment.getCommenter().getId()));
		log.info("Upvote Notification of id '{}' was sent successfully!", saved.getId());
	}

	private boolean validateUpvoteNotification(long count) {
		if (count == 1 || count == 5) {
			// if upvote count is 1 or 5 send notification to comment owner
			return true;
		} else if (count >= 10 && count <= 100 && count % 10 == 0) {
			// if upvote >= 10 and <= 100, send notification on multiples of 10
			return true;
			// if upvote > 100, send notification on multiples of 50
		} else return count > 100 && count % 50 == 0;
	}

	private String getUpvoteDescription(Comment comment) {
		String description = "Your comment on ";
		var suffix = descriptionSuffix(comment.getVoteCount());
		if (comment instanceof NovelReview novelReview) {
			description = description.concat(novelReview.getNovel().capitalizedTitle());
		} else if (comment instanceof ChapterReview chapterReview) {
			description = description.concat(chapterReview.getChapter().getNovel().capitalizedTitle())
					.concat(", %s".formatted(chapterReview.getChapter().getTitle()));
		}
		return description.concat(" %s".formatted(suffix));
	}

	private String descriptionSuffix(Long voteCount) {
		if (voteCount == 1) {
			return "just received its first upvote!";
		}
		return "reached %d upvotes!".formatted(voteCount);
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
