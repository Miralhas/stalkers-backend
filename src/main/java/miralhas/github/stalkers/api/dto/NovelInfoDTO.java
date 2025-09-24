package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

public record NovelInfoDTO(
		Long id,
		String slug,
		String title,
		String author,
		Long chaptersCount,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}