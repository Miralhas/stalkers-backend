package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;

public record CommentInput(
		@NotBlank
		String message,
		Long parentCommentId
) {
}
