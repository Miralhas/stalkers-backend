package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
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
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}