package miralhas.github.stalkers.domain.comment.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.shared.exception.BusinessException;
import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.comment.model.Comment;
import miralhas.github.stalkers.domain.comment.model.Vote;
import miralhas.github.stalkers.domain.comment.model.enums.Type;
import miralhas.github.stalkers.domain.notification.service.NotificationService;
import miralhas.github.stalkers.domain.comment.repository.VoteRepository;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import miralhas.github.stalkers.shared.utils.ValidateAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final ValidateAuthorization validateAuthorization;
	private final VoteRepository voteRepository;
	private final ErrorMessages errorMessages;
	private final NotificationService notificationService;

	@Transactional
	public void createVote(Comment comment, Type voteType) {
		var currentUser = validateAuthorization.getCurrentUser();
		var vote = validateVote(comment, currentUser, voteType);
		voteRepository.save(vote);
//		sendNotification(comment, voteType, vote);
	}

	@Transactional
	public void deleteVote(Long commentId) {
		var currentUser = validateAuthorization.getCurrentUser();
		voteRepository.deleteVoteByUserEmailAndCommentId(currentUser.getEmail(), commentId);
	}

	private void sendNotification(Comment comment, Type voteType, Vote vote) {
		// onwer of the comment upvoting its own comment: shameless! Should not send notification.
		if (vote.getUser().equals(comment.getCommenter())) return;
		if (voteType.equals(Type.DOWNVOTE)) return;
		notificationService.sendUpvoteNotification(comment);
	}

	private Vote validateVote(Comment comment, User user, Type type) {
		var voteOptional = comment.getVotes().stream()
				.filter(up -> up.getUser().getId().equals(user.getId()))
				.findFirst();

		if (voteOptional.isPresent()) {
			var vote = voteOptional.get();
			if (vote.getType().equals(type))  {
				throw new BusinessException(
						errorMessages.get("vote.alreadyVoted", user.getEmail(), type, comment.getId())
				);
			}
			vote.setType(type);
			vote.setCount(type.getCount());
			return vote;
		}

		return Vote.builder()
				.comment(comment)
				.user(user)
				.type(type)
				.count(type.getCount())
				.build();
	}
}
