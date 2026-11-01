package miralhas.github.stalkers.api.user.dto;

import java.time.OffsetDateTime;

public record UserReviewDTO(
		Long id,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		boolean isSpoiler,
		long voteCount,
		String message,
		String type,
		String novelSlug,
		String novelTitle
) {
}
