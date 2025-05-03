package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}