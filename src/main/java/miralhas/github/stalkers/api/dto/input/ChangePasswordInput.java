package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordInput(
		@NotBlank
		@Size(min = 3)
		String newPassword
) {
}
