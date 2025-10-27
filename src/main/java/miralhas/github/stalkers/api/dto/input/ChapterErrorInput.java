package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

public record ChapterErrorInput (
		@NotBlank
		String name,
		@NotBlank
		String description
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}