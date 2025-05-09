package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "notification_type")
@NoArgsConstructor
public class Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@OneToMany(
			mappedBy = "notification",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<NotificationRecipient> recipientAssociations = new HashSet<>();


	public void addRecipient(User user) {
		NotificationRecipient association = new NotificationRecipient(this, user);
		recipientAssociations.add(association);
	}

	public void addRecipients(Set<User> users) {
		var recipients = users.stream().map(u -> new NotificationRecipient(this, u))
				.collect(Collectors.toSet());
		recipientAssociations.addAll(recipients);
	}

	public void removeRecipient(User user) {
		recipientAssociations.removeIf(association -> association.getRecipient().equals(user));
	}

	// Convenience getter to maintain backward compatibility
	public Set<User> getRecipients() {
		return recipientAssociations.stream()
				.map(NotificationRecipient::getRecipient)
				.collect(Collectors.toSet());
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Notification that = (Notification) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
