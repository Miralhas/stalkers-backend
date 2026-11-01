package miralhas.github.stalkers.api.request.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record FixChapterRequestInput(
		@NotNull
		@Size(min = 1)
		Set<String> errors,

		String anotherReason
) {
}
