package miralhas.github.stalkers.api.series.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SeriesInput(
		@NotBlank
		String title,

		@Size(min = 1)
		Set<String> slugs
) {}
