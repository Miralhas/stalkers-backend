package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.metrics.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	@Query("select r from Rating r where r.user.id = :userId AND r.novel.id = :novelId ORDER BY r.id DESC LIMIT 1")
	Optional<Rating> getUserRatingOnNovel(Long userId, Long novelId);
}