package miralhas.github.stalkers.api.dto.input;


import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record UpdateUserInput(
		@Size(min = 3, max = 20)
		String username,

		@Size(min = 3)
		String password
) {
}