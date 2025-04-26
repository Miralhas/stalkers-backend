package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {

	@Query("from Novel n where n.slug = :slug")
	Optional<Novel> findBySlug(String slug);

	@Query(nativeQuery = true,
			value = "SELECT IF(EXISTS(SELECT 1 FROM novel n WHERE n.slug = :slug), 'true', 'false')"
	)
	Boolean checkIfSlugAlreadyExists(String slug);

	@Query(nativeQuery = true, value = "SELECT n.* FROM novel n WHERE n.id = :identifier OR n.slug = :identifier")
	Optional<Novel> findByIdentifier(String identifier);

	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM chapter c WHERE c.novel_id = :novelId ORDER BY c.id")
	Long countNovelChapters(Long novelId);

}