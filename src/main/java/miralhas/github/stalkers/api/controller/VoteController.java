package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.VoteInput;
import miralhas.github.stalkers.domain.model.comment.enums.Type;
import miralhas.github.stalkers.domain.service.ReviewService;
import miralhas.github.stalkers.domain.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/{commentId}/vote")
public class VoteController {

	private final ReviewService reviewService;
	private final VoteService voteService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void voteComment(@PathVariable Long commentId, @RequestBody @Valid VoteInput input) {
		var comment = reviewService.findCommentByIdOrThrowException(commentId);
		voteService.createVote(comment, Type.valueOf(input.voteType()));
	}

	@DeleteMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteVote(@PathVariable Long commentId) {
		voteService.deleteVote(commentId);
	}

}
