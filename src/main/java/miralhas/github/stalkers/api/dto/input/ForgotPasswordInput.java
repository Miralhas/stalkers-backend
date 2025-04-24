package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordInput(
		@Email
		@NotBlank
		@Schema(description = "User email", example = "abc@gmail.com")
		String email
) {
}
