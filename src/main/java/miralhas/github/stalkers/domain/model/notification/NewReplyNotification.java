package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@DiscriminatorValue("NEW_REPLY")
public class NewReplyNotification extends Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Builder
	public NewReplyNotification(
			String userReplying,
			String replyCommentContent,
			String parentCommentContent,
			String uri,
			OffsetDateTime createdAt,
			String title,
			String description,
			Set<User> recipients
	) {
		super(createdAt, title, description, recipients, Type.NEW_REPLY);
		this.userReplying = userReplying;
		this.replyCommentContent = replyCommentContent;
		this.parentCommentContent = parentCommentContent;
		this.uri = uri;
	}

	@Column(nullable = true)
	private String userReplying;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String replyCommentContent;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String parentCommentContent;

	@Column(nullable = true)
	private String uri;
}
