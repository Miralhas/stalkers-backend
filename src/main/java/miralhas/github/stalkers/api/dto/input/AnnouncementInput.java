package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;

public record AnnouncementInput(
		@NotBlank
		String title,

		@NotBlank
		String body,

		boolean pinned

) {
}
