package miralhas.github.stalkers.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserLibraryDTO {
	private Long libraryElementId;
	private String chapterSlug;
	private String novelSlug;
	private Long userId;
	private OffsetDateTime lastReadAt;
	private boolean isBookmarked;
	private boolean isCompleted;
	private Long totalChapters;
}
