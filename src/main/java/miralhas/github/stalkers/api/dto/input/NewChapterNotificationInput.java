package miralhas.github.stalkers.api.dto.input;

import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;

import java.io.Serial;
import java.io.Serializable;

public record NewChapterNotificationInput(
		Chapter chapter,
		Novel novel
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
