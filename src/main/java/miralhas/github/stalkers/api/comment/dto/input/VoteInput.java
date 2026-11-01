package miralhas.github.stalkers.api.comment.dto.input;

import jakarta.validation.constraints.NotBlank;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.comment.model.enums.Type;

public record VoteInput(
		@NotBlank
		@EnumPattern(enumClass = Type.class)
		String voteType
) {
}
