package miralhas.github.stalkers.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CommentDTO(
		Long id,
		Long parentId,
		UserDTO commenter,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		boolean isSpoiler,
		List<String> upvoters,
		String message,
		List<CommentDTO> childComments
) {
}
