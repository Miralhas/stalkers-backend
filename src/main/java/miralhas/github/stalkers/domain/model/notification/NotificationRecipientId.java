package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationRecipientId implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name = "notification_id")
	private Long notificationId;

	@Column(name = "recipient_id")
	private Long recipientId;

	public NotificationRecipientId(Long notificationId, Long recipientId) {
		this.notificationId = notificationId;
		this.recipientId = recipientId;
	}
}
