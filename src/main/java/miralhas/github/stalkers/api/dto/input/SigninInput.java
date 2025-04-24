package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record SigninInput(
		@Email
		@NotBlank
		@Schema(description = "Signin Email", example = "abc@gmail.com")
		String email,

		@NotBlank
		@Size(min = 3)
		@Schema(description = "Signin password", example = "1234")
		String password
) {
}
