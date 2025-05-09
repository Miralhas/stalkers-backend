package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("from Notification n LEFT JOIN FETCH n.recipientAssociations nr where n.id = :id")
	Optional<Notification> findById(Long id);

	@Query(
			nativeQuery = true,
			value = "SELECT COUNT(*) FROM notification n " +
					"LEFT JOIN notification_recipients nr ON n.id = notification_id " +
					"WHERE nr.recipient_id = 2 AND nr.is_read = false;"
	)
	Long findUserUnreadNotificationsCount(Long userId);

}