package miralhas.github.stalkers.api.dto;

import miralhas.github.stalkers.domain.model.comment.Comment;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

public record UserCommentDTO(
		Long id,
		String commenter,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		boolean isSpoiler,
		long voteCount,
		String message,
		String type,
		String slug
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	public boolean isNovelReview() {
		return this.type.equals(Comment.NOVEL_REVIEW);
	}
}
