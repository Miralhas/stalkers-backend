package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

public record NovelSummaryDTO(
		Long id,
		String slug,
		String title,
		String author,
		String status,
		String description,
		Boolean isHidden,
		long chaptersCount,
		Double ratingValue,
		OffsetDateTime createdAt,
		int views,
		Double bayesianScore
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}