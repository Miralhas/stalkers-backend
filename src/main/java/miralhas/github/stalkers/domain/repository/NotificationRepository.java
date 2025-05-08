package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("from Notification n LEFT JOIN FETCH n.recipientAssociations nr where n.id = :id")
	Optional<Notification> findById(Long id);

}