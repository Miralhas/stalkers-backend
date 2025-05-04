package miralhas.github.stalkers.api.dto.input;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.NovelSummaryDTO;

import java.io.Serial;
import java.io.Serializable;

public record NewChapterNotificationInput(
		ChapterSummaryDTO chapter,
		NovelSummaryDTO novel
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
