package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.auth.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification_recipients")
public class NotificationRecipient {

	@EmbeddedId
	private NotificationRecipientId id = new NotificationRecipientId();

	@MapsId("notificationId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@MapsId("recipientId")
	@JoinColumn(name = "recipient_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User recipient;

	@Column(name = "is_read")
	private boolean isRead = false;

	public NotificationRecipient(Notification notification, User recipient) {
		this.notification = notification;
		this.recipient = recipient;
		this.id = new NotificationRecipientId(notification.getId(), recipient.getId());
	}

}
