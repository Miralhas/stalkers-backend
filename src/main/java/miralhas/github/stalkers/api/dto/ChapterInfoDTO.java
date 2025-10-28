package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record ChapterInfoDTO(
		String novelSlug,
		String novelTitle,
		String chapterTitle,
		String chapterSlug,
		Long number
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}