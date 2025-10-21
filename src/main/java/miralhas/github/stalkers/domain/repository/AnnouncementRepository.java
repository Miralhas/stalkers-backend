package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.Announcement;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	@Query(nativeQuery = true,
			value = "SELECT IF(EXISTS(SELECT 1 FROM announcement a WHERE a.slug = :slug), 'true', 'false')"
	)
	boolean checkIfSlugAlreadyExists(String slug);

	@Query("SELECT DISTINCT n FROM Announcement n " +
			"LEFT JOIN FETCH n.user u " +
			"LEFT JOIN FETCH u.roles " +
			"WHERE n.slug = :slug"
	)
	Optional<Announcement> findBySlug(String slug);
}