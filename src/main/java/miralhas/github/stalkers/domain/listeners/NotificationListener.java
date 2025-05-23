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
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class NotificationListener {

	private static final int FIRST_UPVOTE = 1;
	private static final int SECOND_THRESHOLD = 5;
	private static final int MULTIPLE_OF_TEN_START = 10;
	private static final int MULTIPLE_OF_TEN_END = 100;
	private static final int MULTIPLE_OF_FIFTY_START = 101;
	private static final int MULTIPLE_OF_TEN = 10;
	private static final int MULTIPLE_OF_FIFTY = 50;

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
		var capitalizedNovelTitle = CommonsUtils.capitalize(novel.title());

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
		if (isFirstOrSeconThreshold(count)) {
			return true;
		} else if (isMultipleOfTenWithinRange(count)) {
			return true;
		} else return isMultipleOfFiftyWithinRange(count);
	}

	private String getUpvoteDescription(Comment comment) {
		String description = "Your comment on ";
		var suffix = descriptionSuffix(comment.getVoteCount());
		if (comment instanceof NovelReview novelReview) {
			description = description.concat(novelReview.getNovelTitle());
		} else if (comment instanceof ChapterReview chapterReview) {
			description = description.concat(chapterReview.getNovelTitle())
					.concat(", %s".formatted(chapterReview.getChapterTitle()));
		}
		return description.concat(" %s".formatted(suffix));
	}

	private boolean isFirstOrSeconThreshold(long count) {
		return count == FIRST_UPVOTE || count == SECOND_THRESHOLD;
	}

	private boolean isMultipleOfTenWithinRange(long count) {
		return count >= MULTIPLE_OF_TEN_START && count <= MULTIPLE_OF_TEN_END && count % MULTIPLE_OF_TEN == 0;
	}

	private boolean isMultipleOfFiftyWithinRange(long count) {
		return count >= MULTIPLE_OF_FIFTY_START && count % MULTIPLE_OF_FIFTY == 0;
	}

	private String descriptionSuffix(Long voteCount) {
		if (voteCount == FIRST_UPVOTE) {
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
