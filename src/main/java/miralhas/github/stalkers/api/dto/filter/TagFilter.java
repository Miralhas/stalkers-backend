package miralhas.github.stalkers.api.dto.filter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TagFilter(
		@Size(max = 1)
		@Pattern(regexp = "^[a-zA-Z]+$")
		String firstLetter
) {
}
