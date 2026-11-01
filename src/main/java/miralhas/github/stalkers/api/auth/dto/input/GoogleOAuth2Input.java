package miralhas.github.stalkers.api.auth.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GoogleOAuth2Input(
		@Email
		@NotBlank
		String email
) {
}
