package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.filter.LibraryFilter;
import miralhas.github.stalkers.domain.model.UserLibrary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

	@Query("from UserLibrary ul where ul.novel.id = :novelId and ul.user.id = :userId")
	Optional<UserLibrary> findNovelInUserLibraryByUserIdAndNovelId(Long novelId, Long userId);

	@Query("select ul, " +
			"(SELECT COUNT(c) FROM Chapter c WHERE c.novel.id = ul.novel.id) AS totalChapters " +
			"from UserLibrary ul " +
			"JOIN FETCH ul.user " +
			"JOIN FETCH ul.novel " +
			"JOIN FETCH ul.currentChapter " +
			"WHERE ul.user.id = :userId"
	)
	Page<Object[]> findUserLibraryByUserId(Long userId, Pageable pageable);


	@Query("select ul, " +
			"(SELECT COUNT(c) FROM Chapter c WHERE c.novel.id = ul.novel.id) AS totalChapters " +
			"from UserLibrary ul " +
			"JOIN FETCH ul.user " +
			"JOIN FETCH ul.novel " +
			"JOIN FETCH ul.currentChapter " +
			"WHERE ul.user.id = :userId and ul.isBookmarked = true "
	)
	Page<Object[]> findUserLibraryBookmarkByUserId(Long userId, Pageable pageable);

	@Query("select ul, " +
			"(SELECT COUNT(c) FROM Chapter c WHERE c.novel.id = ul.novel.id) AS totalChapters " +
			"from UserLibrary ul " +
			"JOIN FETCH ul.user " +
			"JOIN FETCH ul.novel " +
			"JOIN FETCH ul.currentChapter " +
			"WHERE ul.user.id = :userId and ul.isCompleted = true "
	)
	Page<Object[]> findUserLibraryCompletedByUserId(Long userId, Pageable pageable);


	@Modifying
	@Query("DELETE FROM UserLibrary ul where ul.user.id = :userId and ul.novel.id = :novelId")
	void deleteNovelFromUserLibrary(Long userId, Long novelId);

}