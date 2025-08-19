package miralhas.github.stalkers.api.dto.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import miralhas.github.stalkers.config.validation.EnumPattern;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class LibraryFilter {
	private String novelSlug;

	@EnumPattern(enumClass = LibraryFilterBy.class)
	private String filter;

	public boolean isBookmarked() {
		if (!StringUtils.hasText(filter)) return false;
		return filter.equalsIgnoreCase(LibraryFilterBy.BOOKMARKED.name());
	}

	public boolean isCompleted() {
		if (!StringUtils.hasText(filter)) return false;
		return filter.equalsIgnoreCase(LibraryFilterBy.COMPLETED.name());
	}
}
