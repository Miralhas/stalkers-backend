package miralhas.github.stalkers.api.dto;

import java.time.OffsetDateTime;

public record AnnouncementDTO(
		Long id,
		boolean pinned,
		String slug,
		String title,
		String body,
		UserDTO user,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) {
}
