package miralhas.github.stalkers.api.request.dto;

import miralhas.github.stalkers.api.user.dto.UserDTO;
import miralhas.github.stalkers.domain.request.interfaces.Request;

import java.time.OffsetDateTime;

public record NovelRequest(
		Long id,
		String status,
		String type,
		OffsetDateTime createdAt,
		String novelTitle,
		UserDTO user
) implements Request {
}
