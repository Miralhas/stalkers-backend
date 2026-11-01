package miralhas.github.stalkers.api.comment.dto.input;

import jakarta.validation.constraints.NotBlank;

public record CommentInput(
		@NotBlank
		String message,
		Long parentCommentId,
		Boolean isSpoiler
) {
}
