package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NovelInput(
		@NotBlank
		String title,

		@Valid
		List<ChapterInput> chapters,

		@NotBlank
		String author
) {
}
