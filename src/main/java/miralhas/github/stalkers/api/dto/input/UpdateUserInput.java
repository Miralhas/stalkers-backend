package miralhas.github.stalkers.api.dto.input;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record UpdateUserInput(
		@Size(min = 3, max = 20)
		@Schema(description = "User name", example = "batman420")
		String username,

		@Size(min = 3)
		@Schema(description = "User password", example = "1234")
		String password
) {
}