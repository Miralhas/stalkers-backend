package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;

import java.time.OffsetDateTime;

public record ChapterRequestDTO(
		Long id,
		OffsetDateTime createdAt,
		NovelInfoDTO novel,
		String type,
		UserDTO user,
		String status
) implements RequestDTO {

}
