package miralhas.github.stalkers.api.library.dto.input;

import jakarta.validation.constraints.NotNull;

public record UserLibraryInput(
		@NotNull
		Long novelId,

		@NotNull
		Long chapterId
) {
}
