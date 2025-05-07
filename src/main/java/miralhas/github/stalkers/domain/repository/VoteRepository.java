package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.comment.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long> {

	@Modifying
	@Query("delete from Vote u where u.user.email = :email and u.comment.id = :commentId")
	void deleteVoteByUserEmailAndCommentId(String email, Long commentId);
}