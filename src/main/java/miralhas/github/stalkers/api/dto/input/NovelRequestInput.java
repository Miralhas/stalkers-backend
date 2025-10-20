package miralhas.github.stalkers.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import miralhas.github.stalkers.domain.model.requests.NovelRequest;

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
