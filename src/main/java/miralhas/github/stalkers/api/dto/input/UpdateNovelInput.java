package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.Size;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.model.novel.enums.Status;

import java.util.List;

public record UpdateNovelInput(
		String title,

		String author,

		@EnumPattern(enumClass = Status.class)
		String status,

		String description,

		@Size(min = 1)
		List<String> genres,

		@Size(min = 1)
		List<String> tags
) {
}
