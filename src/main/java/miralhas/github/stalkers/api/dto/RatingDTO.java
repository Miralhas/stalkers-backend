package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record RatingDTO(
		Double ratingValue,
		int ratingSize
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
