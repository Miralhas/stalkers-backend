package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;

public record UpdateSeriesInput(
		@NotBlank
		String title
) {
}
