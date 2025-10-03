package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record AuthorDTO(
		String name,
		Long novelsCount
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}