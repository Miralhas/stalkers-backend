package miralhas.github.stalkers.api.chapter.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BulkChaptersInput(
		@Valid
		@Size(min = 1)
		List<ChapterInput> chapters
) {
}
