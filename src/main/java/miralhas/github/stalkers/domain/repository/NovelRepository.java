package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface NovelRepository extends JpaRepository<Novel, Long>, JpaSpecificationExecutor<Novel> {

	@Query("from Novel n " +
			"LEFT JOIN FETCH n.tags " +
			"LEFT JOIN FETCH n.genres " +
			"where n.slug = :slug")
	Optional<Novel> findBySlug(String slug);

	@Query(nativeQuery = true,
			value = "SELECT IF(EXISTS(SELECT 1 FROM novel n WHERE n.slug = :slug), 'true', 'false')"
	)
	Boolean checkIfSlugAlreadyExists(String slug);

	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM chapter c WHERE c.novel_id = :novelId ORDER BY c.id")
	Long countNovelChapters(Long novelId);

	@Query(value = "select " +
			"new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
			"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
			"from Novel n LEFT JOIN Chapter c on c.novel.id = n.id " +
			"WHERE n.id = :id ORDER BY c.id ASC LIMIT 1"
	)
	ChapterSummaryDTO findNovelFirstChapterByNovelId(Long id);

	@Query(value = "select " +
			"new miralhas.github.stalkers.api.dto.ChapterSummaryDTO(" +
			"c.id, c.title, c.slug, c.number, c.createdAt, c.updatedAt) " +
			"from Novel n LEFT JOIN Chapter c on c.novel.id = n.id " +
			"WHERE n.id = :id ORDER BY c.id DESC LIMIT 1"
	)
	ChapterSummaryDTO findNovelLastChapterByNovelId(Long id);

	@Query("select ul.user.id " +
			"from UserLibrary ul " +
			"where ul.novel.id = :novelId and ul.isBookmarked = true")
	Set<Long> findAllBookmarkedUsersIdOfANovel(Long novelId);

	@Modifying
	@Query("delete from Novel n where n.slug = :slug ")
	void deleteBySlug(String slug);

}