package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("NEW_REPLY")
public class NewReplyNotification extends Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private String userReplying;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String replyCommentContent;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String parentCommentContent;

	public void setType() {
		super.setType(NotificationType.NEW_REPLY);
	}
}
