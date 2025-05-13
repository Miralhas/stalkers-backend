package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserInput (
		@Email
		@NotBlank
		@Schema(description = "User email", example = "abc@gmail.com")
		String email,

		@NotBlank
		@Size(min = 3, max = 20)
		@Pattern(regexp = "^[a-zA-Z0-9_]*$")
		@Schema(description = "User name", example = "batman420")
		String username,

		@NotBlank
		@Size(min = 3)
		@Schema(description = "User password", example = "1234")
		String password
) {}
