package miralhas.github.stalkers.api.comment.dto.input;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentInput(
		@NotBlank
		String message,

		Boolean isSpoiler
) {
}
