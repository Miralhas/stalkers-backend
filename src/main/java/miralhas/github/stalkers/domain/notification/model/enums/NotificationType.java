package miralhas.github.stalkers.domain.notification.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	NEW_CHAPTER("NEW_CHAPTER"),
	NEW_VOTE("NEW_VOTE"),
	NEW_REPLY("NEW_REPLY");

	private final String name;
}
