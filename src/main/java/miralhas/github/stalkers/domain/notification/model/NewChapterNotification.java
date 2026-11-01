package miralhas.github.stalkers.domain.notification.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import miralhas.github.stalkers.domain.notification.model.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("NEW_CHAPTER")
public class NewChapterNotification extends Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private String newChapterSlug;

	@Column(nullable = true)
	private String novelSlug;

	public void setType() {
		super.setType(NotificationType.NEW_CHAPTER);
	}
}
