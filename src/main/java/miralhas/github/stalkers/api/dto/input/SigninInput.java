package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record SigninInput(
		@Email
		@NotBlank
		String email,

		@NotBlank
		@Size(min = 3)
		String password
) {
}
