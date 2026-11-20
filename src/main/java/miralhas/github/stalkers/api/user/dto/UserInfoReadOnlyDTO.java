package miralhas.github.stalkers.api.user.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record UserInfoReadOnlyDTO(
		Long id,
		String username,
		OffsetDateTime createdAt,
		List<String> roles,
		Long readCount,
		Long bookmarkCount,
		Long completedCount,
		Long reviewsCount,
		OffsetDateTime lastActivity
) {
}
