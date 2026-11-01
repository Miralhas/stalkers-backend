package miralhas.github.stalkers.api.user.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record UserInfoDTO(
		Long id,
		String username,
		String email,
		OffsetDateTime createdAt,
		List<String> roles,
		Long readCount,
		Long bookmarkCount,
		Long completedCount,
		Long reviewsCount,
		OffsetDateTime lastActivity,
		String registerType
) {
}
