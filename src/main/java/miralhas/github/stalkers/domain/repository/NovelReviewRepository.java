package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.NovelReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NovelReviewRepository extends JpaRepository<NovelReview, Long> {
	@Query("SELECT nr from NovelReview nr " +
			"LEFT JOIN FETCH nr.childComments " +
			"LEFT JOIN FETCH nr.parentComment " +
			"WHERE nr.novel.slug = :slug " +
			"AND nr.parentComment.id IS NULL"
	)
	List<NovelReview> findRootReviewsByNovelSlug(String slug);
}