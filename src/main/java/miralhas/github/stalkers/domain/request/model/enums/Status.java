package miralhas.github.stalkers.domain.request.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
	PENDING("Pending"),
	DENIED("Denied"),
	COMPLETED("Completed");
	private final String value;
}
