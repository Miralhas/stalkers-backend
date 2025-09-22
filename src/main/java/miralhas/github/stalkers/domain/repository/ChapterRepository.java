package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
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

	@Query(value = """
        SELECT c.id, n.id, c.slug, c.number, c.title, n.author, n.title, n.slug, c.created_at
        FROM chapter c
        JOIN (
            SELECT novel_id, MAX(created_at) AS latest_created_at
            FROM chapter
            GROUP BY novel_id
        ) AS latest ON c.novel_id = latest.novel_id AND c.created_at = latest.latest_created_at
        JOIN novel n ON n.id = c.novel_id
        ORDER BY c.created_at DESC, c.number DESC
        LIMIT 45
        """, nativeQuery = true)
	List<Object[]> findAllLatestChaptersDTO();

	@Query("SELECT c FROM Chapter c LEFT JOIN FETCH c.novel WHERE c.slug = :slug")
	Optional<Chapter> findBySlug(String slug);

	@Modifying
	@Query("delete from Chapter c where c.slug = :slug")
	void deleteBySlug(String slug);

	@Query("FROM Chapter c where c.novel.slug = :novelSlug AND c.number = :number")
	Optional<Chapter> findByNovelSlugAndChapterNumber(String novelSlug, Long number);
}