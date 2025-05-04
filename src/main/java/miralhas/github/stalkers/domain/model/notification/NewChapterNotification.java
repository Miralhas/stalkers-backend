package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.enums.Type;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("NEW_CHAPTER")
public class NewChapterNotification extends Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Builder
	public NewChapterNotification(
			String newChapterSlug,
			String novelSlug,
			OffsetDateTime newChapterReleaseDate,
			OffsetDateTime createdAt,
			String title,
			String description,
			Set<User> recipients
	) {
		super(createdAt, title, description, recipients, Type.NEW_CHAPTER);
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
