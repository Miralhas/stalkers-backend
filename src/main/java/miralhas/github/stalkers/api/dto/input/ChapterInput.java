package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChapterInput(
		@NotBlank
		@Size(max = 250)
		String title,

		@NotBlank
		String body,

		@NotNull
		Long number
) {
}
