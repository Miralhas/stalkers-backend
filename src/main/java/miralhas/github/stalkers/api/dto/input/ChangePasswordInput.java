package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordInput(
		@NotBlank
		@Size(min = 3)
		@Schema(description = "Signin password", example = "1234")
		String newPassword
) {
}
