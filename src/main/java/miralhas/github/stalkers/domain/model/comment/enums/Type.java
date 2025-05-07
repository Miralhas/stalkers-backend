package miralhas.github.stalkers.domain.model.comment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
	UPVOTE(Constants.UPVOTE_COUNT_VALUE),
	DOWNVOTE(Constants.DOWNVOTE_COUNT_VALUE);

	private final int count;

	private static class Constants {
		public static final int UPVOTE_COUNT_VALUE = 1;
		public static final int DOWNVOTE_COUNT_VALUE = -1;
	}
}
