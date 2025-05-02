package miralhas.github.stalkers.api.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryFilter {
	private Boolean completed = false;
	private Boolean bookmarked = false;
}
