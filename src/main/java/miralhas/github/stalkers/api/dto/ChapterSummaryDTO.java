package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record ChapterSummaryDTO(
		Long id,
		String title,
		String slug,
		Long number
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}

