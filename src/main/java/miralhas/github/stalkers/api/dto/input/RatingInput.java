package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import miralhas.github.stalkers.config.validation.RatingRange;

public record RatingInput(

		@Min(0)
		@Max(5)
		@NotNull
		@RatingRange
		Double ratingValue
) {
}
