package miralhas.github.stalkers.api.request.dto.input;

import jakarta.validation.constraints.NotBlank;
import miralhas.github.stalkers.domain.request.model.NovelRequest;

public record NovelRequestInput(
		@NotBlank
		String novelTitle
) {
	public NovelRequest toNovelRequest() {
		var novelRequest = new NovelRequest();
		novelRequest.setNovelTitle(this.novelTitle);
		return novelRequest;
	}
}
