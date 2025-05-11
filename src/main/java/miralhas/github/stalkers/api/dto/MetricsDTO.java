package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record MetricsDTO(
		int views,
		Double ratingValue,
		int ratingSize,
		Double bayesianScore
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
