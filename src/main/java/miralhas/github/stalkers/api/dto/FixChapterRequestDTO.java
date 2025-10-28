package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;
import miralhas.github.stalkers.domain.model.requests.ChapterError;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public record FixChapterRequestDTO(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		UserDTO user,
		ChapterInfoDTO chapterInfoDTO,
		Set<ChapterError> errors,
		String anotherReason
) implements Serializable, RequestDTO {
	@Serial
	private static final long serialVersionUID = 1L;
}
