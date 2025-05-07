package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.domain.model.comment.enums.Type;

import java.time.OffsetDateTime;

public record UserCommentDTO(
		Long id,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		boolean isSpoiler,
		long voteCount,
		String message,
		String type,
		String slug
) {
}
