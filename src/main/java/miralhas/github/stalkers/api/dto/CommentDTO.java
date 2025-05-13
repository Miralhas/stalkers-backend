package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public record CommentDTO(
		Long id,
		Long parentId,
		UserDTO commenter,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		boolean isSpoiler,
		List<String> voters,
		long voteCount,
		String message,
		List<CommentDTO> childComments
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}