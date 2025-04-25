package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotNull;

public record ChapterInput(
		@NotNull
		String title,

		@NotNull
		String body
) {
}
