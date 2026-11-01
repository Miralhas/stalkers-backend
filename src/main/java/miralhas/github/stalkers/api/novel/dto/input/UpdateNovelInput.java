package miralhas.github.stalkers.api.novel.dto.input;

import jakarta.validation.constraints.Size;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.novel.model.enums.Status;

import java.util.List;

public record UpdateNovelInput(
		String title,

		String author,

		@Size(min = 2)
		String alias,

		@EnumPattern(enumClass = Status.class)
		String status,

		String description,

		@Size(min = 1)
		List<String> genres,

		@Size(min = 1)
		List<String> tags
) {
}
