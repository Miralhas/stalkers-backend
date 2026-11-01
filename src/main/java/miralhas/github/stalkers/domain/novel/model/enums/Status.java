package miralhas.github.stalkers.domain.novel.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
	COMPLETED("Completed"),
	ON_GOING("On going"),;
	private final String value;
}
