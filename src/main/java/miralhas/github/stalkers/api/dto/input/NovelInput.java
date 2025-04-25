package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NovelInput(
		@NotBlank
		String title,

		@Valid
		List<ChapterInput> chapters,

		@NotNull
		String author
) {
}
