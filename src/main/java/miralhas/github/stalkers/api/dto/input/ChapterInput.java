package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChapterInput(
		@NotBlank
		String title,

		@NotBlank
		String body,

		@NotNull
		Long number
) {
}
