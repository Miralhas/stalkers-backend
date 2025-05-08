package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("from User u LEFT JOIN FETCH u.roles where u.email = :email")
	Optional<User> findUserByEmail(String email);

	@Query("from User u LEFT JOIN FETCH u.roles where u.username = :username")
	Optional<User> findUserByUsername(String username);

	@Override
	@Query("from User u LEFT JOIN FETCH u.roles")
	List<User> findAll();

	@Query("select n from Notification n LEFT JOIN n.recipients u WHERE u.id = :userId ORDER BY n.createdAt DESC")
	List<Notification> findUserNotifications(Long userId);

	@Query("select new miralhas.github.stalkers.api.dto.UserCommentDTO(" +
			"c.id, c.commenter.email, c.createdAt, c.updatedAt, c.isSpoiler, c.voteCount, c.message, 'CHAPTER_REVIEW', c.chapter.slug) " +
			"from ChapterReview c " +
			"WHERE c.commenter.id = :userId ORDER BY c.createdAt DESC")
	List<UserCommentDTO> findAllUserChapterComments(Long userId);

	@Query("select new miralhas.github.stalkers.api.dto.UserCommentDTO(" +
			"c.id, c.commenter.email, c.createdAt, c.updatedAt, c.isSpoiler, c.voteCount, c.message, 'NOVEL_REVIEW', c.novel.slug) " +
			"from NovelReview c " +
			"WHERE c.commenter.id = :userId ORDER BY c.createdAt DESC")
	List<UserCommentDTO> findAllUserNovelComments(Long userId);

}