package miralhas.github.stalkers.api.chapter.dto;

import miralhas.github.stalkers.api.novel.dto.NovelInfoDTO;
import miralhas.github.stalkers.api.user.dto.UserDTO;
import miralhas.github.stalkers.domain.request.interfaces.Request;

import java.time.OffsetDateTime;

public record ChapterRequest(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		NovelInfoDTO novel,
		UserDTO user
) implements Request {

}
