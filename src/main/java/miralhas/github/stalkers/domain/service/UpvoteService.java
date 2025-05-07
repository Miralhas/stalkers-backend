package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.Upvote;
import miralhas.github.stalkers.domain.repository.UpvoteRepository;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpvoteService {

	private final ValidateAuthorization validateAuthorization;
	private final UpvoteRepository upvoteRepository;

	@Transactional
	public void createUpvote(Comment comment) {
		var currentUser = validateAuthorization.getCurrentUser();
		validateUpvote(comment, currentUser);
		var upvote = Upvote.builder()
				.comment(comment)
				.user(currentUser)
				.build();
		upvoteRepository.save(upvote);
	}

	@Transactional
	public void deleteUpvote(Long commentId) {
		var currentUser = validateAuthorization.getCurrentUser();
		upvoteRepository.deleteUpvoteByUserEmailAndCommentId(currentUser.getEmail(), commentId);
	}

	private void validateUpvote(Comment comment, User user) {
		var hasUserAlreadyUpvoted = comment.getUpvotes()
				.stream()
				.anyMatch(up -> up.getUser().getId().equals(user.getId()));
		if (!hasUserAlreadyUpvoted) return;
		throw new BusinessException("User already upvoted the comment of id '%s'".formatted(comment.getId()));
	}
}
