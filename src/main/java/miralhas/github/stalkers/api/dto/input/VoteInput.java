package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.model.comment.enums.Type;

public record VoteInput(
		@NotBlank
		@EnumPattern(enumClass = Type.class)
		String voteType
) {
}
