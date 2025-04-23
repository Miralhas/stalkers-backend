package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record RefreshTokenInput(
		@UUID
		@NotBlank
		@Schema(description = "Refresh token to retrieve new access tokens")
		String refreshToken
) {
}
