package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChapterReviewRepository extends JpaRepository<ChapterReview, Long> {
	@Query("SELECT cr from ChapterReview cr " +
			"LEFT JOIN FETCH cr.childComments ch " +
			"LEFT JOIN FETCH cr.parentComment " +
			"LEFT JOIN FETCH cr.votes up " +
			"LEFT JOIN FETCH cr.commenter co " +
			"LEFT JOIN FETCH up.user u " +
			"WHERE cr.chapter.slug = :slug " +
			"AND cr.parentComment.id IS NULL"
	)
	Page<ChapterReview> findRootReviewsByChapterSlug(String slug, Pageable pageable);
}