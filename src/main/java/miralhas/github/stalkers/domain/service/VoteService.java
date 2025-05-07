package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.Vote;
import miralhas.github.stalkers.domain.model.comment.enums.Type;
import miralhas.github.stalkers.domain.repository.VoteRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final ValidateAuthorization validateAuthorization;
	private final VoteRepository voteRepository;
	private final ErrorMessages errorMessages;

	@Transactional
	public void createVote(Comment comment, Type voteType) {
		var currentUser = validateAuthorization.getCurrentUser();
		validateVote(comment, currentUser);
		var upvote = Vote.builder()
				.comment(comment)
				.user(currentUser)
				.type(voteType)
				.count(voteType.getCount())
				.build();
		voteRepository.save(upvote);
	}

	@Transactional
	public void deleteVote(Long commentId) {
		var currentUser = validateAuthorization.getCurrentUser();
		voteRepository.deleteVoteByUserEmailAndCommentId(currentUser.getEmail(), commentId);
	}

	private void validateVote(Comment comment, User user) {
		var hasUserAlreadyVoted = comment.getVotes()
				.stream()
				.anyMatch(up -> up.getUser().getId().equals(user.getId()));
		if (!hasUserAlreadyVoted) return;
		throw new BusinessException(
				errorMessages.get("vote.alreadyVoted", user.getEmail(), comment.getId())
		);
	}
}
