package miralhas.github.stalkers.api.dto.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LibraryFilter {
	private Boolean completed = false;
	private Boolean bookmarked = false;
}
