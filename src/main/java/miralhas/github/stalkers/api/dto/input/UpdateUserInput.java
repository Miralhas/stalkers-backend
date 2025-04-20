package miralhas.github.stalkers.api.dto.input;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record UpdateUserInput(
		@NotBlank
		@Size(min = 3, max = 20)
		String username,

		@NotBlank
		@Size(min = 3)
		String password
) {
}