package miralhas.github.stalkers.api.request.dto;

import miralhas.github.stalkers.api.chapter.dto.ChapterInfoDTO;
import miralhas.github.stalkers.api.user.dto.UserDTO;
import miralhas.github.stalkers.domain.request.interfaces.Request;
import miralhas.github.stalkers.domain.request.model.ChapterError;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public record FixChapterRequest(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		UserDTO user,
		ChapterInfoDTO chapterInfoDTO,
		Set<ChapterError> errors,
		String anotherReason
) implements Serializable, Request {
	@Serial
	private static final long serialVersionUID = 1L;
}
