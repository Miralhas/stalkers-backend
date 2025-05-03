package miralhas.github.stalkers.domain.model.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
	NEW_CHAPTER("NEW_CHAPTER"),
	UPVOTE("UPVOTE"),
	REPLY("REPLY");

	private final String name;
}
