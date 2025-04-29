package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NovelInput(
		@NotBlank
		String title,

		@Valid
		List<ChapterInput> chapters,

		@NotBlank
		String author,

		@NotBlank
		String description,

		@NotNull
		@Size(min = 1)
		List<String> genres,

		@NotNull
		@Size(min = 1)
		List<String> tags
) {
}
