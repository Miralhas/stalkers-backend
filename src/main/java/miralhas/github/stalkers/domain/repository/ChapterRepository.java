package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
	@Query(nativeQuery = true,
			value = "SELECT case when EXISTS(SELECT 1 FROM chapter c WHERE c.slug = :slug LIMIT 1) then 'true' ELSE 'false' END;"
	)
	Boolean checkIfSlugAlreadyExists(String slug);

	@Query(value = "SELECT new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(c.id, c.title, c.slug, c.number) " +
			"FROM Chapter c LEFT JOIN Novel n ON n.id = c.novel.id WHERE n.slug = :slug")
	List<ChapterSummaryDTO> findAllByNovelSlug(String slug);

	@Query(nativeQuery = true, value = "SELECT c.* FROM chapter c WHERE c.slug = :slug")
	Optional<Chapter> findBySlug(String slug);

	@Modifying
	@Query("delete from Chapter c where c.slug = :slug")
	void deleteBySlug(String slug);
}