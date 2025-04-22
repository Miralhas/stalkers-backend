package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordInput(
		@Email
		@NotBlank
		String email
) {
}
