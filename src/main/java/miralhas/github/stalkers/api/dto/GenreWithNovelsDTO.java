package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.domain.model.novel.Genre;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record GenreWithNovelsDTO(
		Genre genre,
		List<String> novelSlugs
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
