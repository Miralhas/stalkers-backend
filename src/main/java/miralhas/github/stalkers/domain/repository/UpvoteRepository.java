package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

	@Modifying
	@Query("delete from Upvote u where u.user.email = :email and u.comment.id = :commentId")
	void deleteUpvoteByUserEmailAndCommentId(String email, Long commentId);
}