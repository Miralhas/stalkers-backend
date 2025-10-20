package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;

import java.time.OffsetDateTime;

public record ChapterRequestDTO(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		NovelInfoDTO novel,
		UserDTO user
) implements RequestDTO {

}
