package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c " +
			"LEFT JOIN FETCH c.parentComment " +
			"LEFT JOIN FETCH c.childComments " +
			"LEFT JOIN FETCH c.votes " +
			"WHERE c.parentComment.id = :parentId"
	)
	List<Comment> findByParentCommentId(Long parentId);

	@Query("SELECT c FROM Comment c " +
			"LEFT JOIN FETCH c.parentComment " +
			"LEFT JOIN FETCH c.childComments " +
			"LEFT JOIN FETCH c.votes " +
			"WHERE c.id = :commentId"
	)
	Optional<Comment> findById(Long commentId);
}