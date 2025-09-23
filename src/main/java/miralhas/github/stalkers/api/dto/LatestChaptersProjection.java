package miralhas.github.stalkers.api.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

public interface LatestChaptersProjection {
	Long getId();
	Long getNovelId();
	String getSlug();
	Long getChapterNumber();
	String getTitle();
	String getAuthor();
	String getNovelTitle();
	String getNovelSlug();
	LocalDateTime getCreatedAt();

	default LatestChapterDTO getLatestChaptersDTO() {
		var createdAt = Objects.isNull(getCreatedAt()) ? null :
				getCreatedAt().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime();

		return new LatestChapterDTO(
				getId(),
				getNovelId(),
				getSlug(),
				getChapterNumber(),
				getTitle(),
				getAuthor(),
				getNovelTitle(),
				getNovelSlug(),
				createdAt
		);
	}
}
