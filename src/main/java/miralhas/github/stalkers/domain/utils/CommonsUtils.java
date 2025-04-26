package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonsUtils {

	public static String getInitialsFromSlug(String slug) {
		String[] words = slug.split("-");
		StringBuilder initials = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				initials.append(word.charAt(0));
			}
		}
		return initials.toString();
	}

}
