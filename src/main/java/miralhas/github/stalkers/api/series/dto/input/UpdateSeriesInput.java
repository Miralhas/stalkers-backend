package miralhas.github.stalkers.api.series.dto.input;

import jakarta.validation.constraints.NotBlank;

public record UpdateSeriesInput(
		@NotBlank
		String title
) {
}
