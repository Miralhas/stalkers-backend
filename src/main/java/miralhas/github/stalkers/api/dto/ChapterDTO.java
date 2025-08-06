package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record ChapterDTO(
		Long id,
		String title,
		String slug,
		String body,
		String novelSlug
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
