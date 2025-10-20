package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;

import java.time.OffsetDateTime;

public record NovelRequestDTO(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		String novelTitle,
		UserDTO user
) implements RequestDTO {
}
