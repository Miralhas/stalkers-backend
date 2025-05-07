package miralhas.github.stalkers.api.controller;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto_mapper.CommentMapper;
import miralhas.github.stalkers.domain.service.ReviewService;
import miralhas.github.stalkers.domain.service.UpvoteService;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/{commentId}/upvote")
public class UpvoteController {

	private final ReviewService reviewService;
	private final UpvoteService upvoteService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void upvoteComment(@PathVariable Long commentId) {
		var comment = reviewService.findCommentByIdOrThrowException(commentId);
		upvoteService.createUpvote(comment);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUpvote(@PathVariable Long commentId) {
		upvoteService.deleteUpvote(commentId);
	}

}
