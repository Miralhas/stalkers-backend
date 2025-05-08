package miralhas.github.stalkers.api.dto.input;

import miralhas.github.stalkers.api.dto.UserCommentDTO;

import java.io.Serial;
import java.io.Serializable;

public record NewReplyNotificationInput(
		UserCommentDTO parentComment,
		UserCommentDTO childComment
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
