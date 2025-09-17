package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.NovelReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NovelReviewRepository extends JpaRepository<NovelReview, Long> {
	@Query("SELECT nr from NovelReview nr " +
			"LEFT JOIN FETCH nr.childComments ch " +
			"LEFT JOIN FETCH nr.parentComment " +
			"LEFT JOIN FETCH nr.votes up " +
			"LEFT JOIN FETCH nr.commenter co " +
			"WHERE nr.novel.slug = :slug " +
			"AND nr.parentComment.id IS NULL"
	)
	Page<NovelReview> findRootReviewsByNovelSlug(String slug, Pageable pageable);
}