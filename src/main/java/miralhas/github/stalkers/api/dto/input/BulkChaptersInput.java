package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.Valid;

import java.util.List;

public record BulkChaptersInput(
		@Valid
		List<ChapterInput> chapters
) {
}
