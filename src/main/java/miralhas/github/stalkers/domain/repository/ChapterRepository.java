package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.ChapterSlimProjection;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.LatestChapterDTO;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query(
			value = "SELECT new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
					"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
					"FROM Chapter c WHERE c.novel.slug = :slug"
	)
	Page<ChapterSummaryDTO> findAllByNovelSlug(String slug, Pageable pageable);

	@Query(value = "select " +
			"new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
			"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
			"FROM Chapter c " +
			"WHERE c.novel.id = :novelId AND c.number = :chapterNumber-1 ORDER BY c.id LIMIT 1"
	)
	ChapterSummaryDTO findPreviousChapter(Long novelId, Long chapterNumber);

	@Query(value = "select " +
			"new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
			"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
			"FROM Chapter c " +
			"WHERE c.novel.id = :novelId AND c.number = :chapterNumber+1 ORDER BY c.id LIMIT 1"
	)
	ChapterSummaryDTO findNextChapter(Long novelId, Long chapterNumber);

	@Query("SELECT MAX(c.id) FROM Chapter c GROUP BY c.novel.id ORDER BY MAX(c.createdAt) DESC")
	List<Long> findAllLastestChaptersIDS();

	@Query("""
		SELECT new miralhas.github.stalkers.api.dto.LatestChapterDTO(
			c.id, c.novel.id, c.slug, c.number, c.title, c.novel.author, c.novel.title, c.novel.slug, c.createdAt
		) FROM Chapter c WHERE c.id IN (:ids)
	""")
	Page<LatestChapterDTO> findLatestChapters(List<Long> ids, Pageable pageable);

	@Query("SELECT c FROM Chapter c LEFT JOIN FETCH c.novel WHERE c.slug = :slug")
	Optional<Chapter> findBySlug(String slug);

	@Modifying
	@Query("delete from Chapter c where c.slug = :slug")
	void deleteBySlug(String slug);

	@Query("FROM Chapter c where c.novel.slug = :novelSlug AND c.number = :number")
	Optional<Chapter> findByNovelSlugAndChapterNumber(String novelSlug, Long number);

	@Query("FROM Chapter c left join fetch c.novel " +
		"where c.novel.slug = :novelSlug AND c.number BETWEEN :min AND :max"
	)
	List<Chapter> chaptersBetweenRange(String novelSlug, int min, int max);

	@Query("FROM Chapter c left join fetch c.novel " +
			"where c.novel.slug = :novelSlug AND c.number BETWEEN :min AND :max"
	)
	List<ChapterSlimProjection> slimChaptersBetweenRange(String novelSlug, int min, int max);
}