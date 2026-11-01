package miralhas.github.stalkers.api.notification.dto.input;

import miralhas.github.stalkers.api.user.dto.UserCommentDTO;

import java.io.Serial;
import java.io.Serializable;

public record NewReplyNotificationInput(
		UserCommentDTO parentComment,
		UserCommentDTO childComment
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
