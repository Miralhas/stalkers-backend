package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("NEW_CHAPTER")
public class NewChapterNotification extends Notification {

	@Builder
	public NewChapterNotification(
			String newChapterSlug,
			String novelSlug,
			OffsetDateTime newChapterReleaseDate,
			boolean isRead,
			OffsetDateTime createdAt,
			String title,
			String description,
			Set<User> recipients
	) {
		super(isRead, createdAt, title, description, recipients);
		this.newChapterSlug = newChapterSlug;
		this.novelSlug = novelSlug;
		this.newChapterReleaseDate = newChapterReleaseDate;
	}

	@Column(nullable = true)
	private String newChapterSlug;

	@Column(nullable = true)
	private String novelSlug;

	@Column(nullable = true)
	private OffsetDateTime newChapterReleaseDate;
}
