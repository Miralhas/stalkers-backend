package miralhas.github.stalkers.api.dto;

import java.time.OffsetDateTime;

public record UserInfoDTO(
		Long id,
		String username,
		String email,
		String roles,
		Long readCount,
		Long bookmarkCount,
		Long completedCount,
		Long reviewsCount,
		OffsetDateTime lastActivity,
		String registerType
) {
}
