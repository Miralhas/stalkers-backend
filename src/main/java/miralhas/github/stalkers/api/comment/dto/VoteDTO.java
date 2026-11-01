package miralhas.github.stalkers.api.comment.dto;

import miralhas.github.stalkers.domain.comment.model.enums.Type;

import java.io.Serial;
import java.io.Serializable;

public record VoteDTO (
		String voter,
		Type type
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}