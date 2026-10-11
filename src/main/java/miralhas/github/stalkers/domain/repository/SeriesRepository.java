package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.series.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

	@Query(nativeQuery = true,
			value = "SELECT IF(EXISTS(SELECT 1 FROM series a WHERE a.slug = :slug), 'true', 'false')"
	)
	boolean checkIfSlugAlreadyExists(String slug);

	@Query("SELECT DISTINCT n FROM Series n " +
			"WHERE n.slug = :slug"
	)
	Optional<Series> findBySlug(String slug);
}