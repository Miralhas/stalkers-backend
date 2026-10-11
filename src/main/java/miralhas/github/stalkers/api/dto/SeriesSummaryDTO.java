package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record SeriesSummaryDTO(
		String title,
		String slug
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}