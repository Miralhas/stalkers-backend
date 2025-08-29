package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.model.novel.enums.Status;

import java.util.List;

public record NovelInput(
		@NotBlank
		String title,

		@NotBlank
		String author,

		@EnumPattern(enumClass = Status.class)
		String status,

		@NotBlank
		String description,

		String alias,

		@Valid
		@NotNull
		List<ChapterInput> chapters,

		@NotNull
		@Size(min = 1)
		List<String> genres,

		@NotNull
		@Size(min = 1)
		List<String> tags
) {
}
