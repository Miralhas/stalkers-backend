package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
	@Query(nativeQuery = true,
			value = "SELECT case when EXISTS(SELECT 1 FROM chapter c WHERE c.slug = :slug LIMIT 1) then 'true' ELSE 'false' END;"
	)
	Boolean checkIfSlugAlreadyExists(String slug);

	@Query(
			value = "SELECT new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
			"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
			"FROM Chapter c WHERE c.novel.slug = :slug"
	)
	Page<ChapterSummaryDTO> findAllByNovelSlug(String slug, Pageable pageable);

	@Query("SELECT c FROM Chapter c LEFT JOIN FETCH c.novel WHERE c.slug = :slug")
	Optional<Chapter> findBySlug(String slug);

	@Modifying
	@Query("delete from Chapter c where c.slug = :slug")
	void deleteBySlug(String slug);
}