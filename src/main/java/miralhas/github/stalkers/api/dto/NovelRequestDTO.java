package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;

import java.time.OffsetDateTime;

public record NovelRequestDTO(
		Long id,
		OffsetDateTime createdAt,
		String novelTitle,
		String type,
		String status,
		UserDTO user
) implements RequestDTO {
}
