package miralhas.github.stalkers.api.notification.dto.input;

import miralhas.github.stalkers.api.chapter.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.novel.dto.NovelSummaryDTO;

import java.io.Serial;
import java.io.Serializable;

public record NewChapterNotificationInput(
		ChapterSummaryDTO chapter,
		NovelSummaryDTO novel
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
