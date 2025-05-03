package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotNull;

public record UserLibraryInput(
		@NotNull
		Long novelId,

		@NotNull
		Long chapterId
) {
}
