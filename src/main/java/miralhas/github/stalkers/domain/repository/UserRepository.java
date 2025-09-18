package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.api.dto.UserChapterCommentDTO;
import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.api.dto.UserInfoProjection;
import miralhas.github.stalkers.api.dto.UserReviewDTO;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("select n from Notification n LEFT JOIN n.recipientAssociations nr WHERE nr.recipient.id = :userId ORDER BY n.createdAt DESC")
	List<Notification> findUserNotifications(Long userId);

	@Query("select new miralhas.github.stalkers.api.dto.UserChapterCommentDTO(" +
			"c.id, c.createdAt, c.updatedAt, c.isSpoiler, c.voteCount, c.message, 'CHAPTER_REVIEW'," +
			" c.chapter.novel.slug," +
			" c.chapter.novel.title," +
			" c.chapter.slug," +
			" c.chapter.title) " +
			"from ChapterReview c " +
			"WHERE c.commenter.id = :userId")
	Page<UserChapterCommentDTO> findAllUserChapterComments(Long userId, Pageable pageable);

	@Query("select new miralhas.github.stalkers.api.dto.UserReviewDTO(" +
			"c.id, c.createdAt, c.updatedAt, c.isSpoiler, c.voteCount, c.message, 'NOVEL_REVIEW', c.novel.slug, c.novel.title) " +
			"from NovelReview c " +
			"WHERE c.commenter.id = :userId")
	Page<UserReviewDTO> findAllUserNovelReviews(Long userId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT  u.id, u.username, u.email, u.created_at, " +
			"(SELECT GROUP_CONCAT(r.`name` SEPARATOR ',') FROM user_roles ur LEFT JOIN role r ON r.id = ur.role_id WHERE ur.user_id = u.id) AS roles, " +
			"(SELECT DISTINCT COUNT(*) FROM user_library ul WHERE ul.user_id = u.id) AS readCount, " +
			"(SELECT COUNT(*) FROM user_library ul WHERE ul.user_id = u.id AND ul.is_bookmarked) AS bookmarkCount, " +
			"(SELECT COUNT(*) FROM user_library ul WHERE ul.user_id = u.id AND ul.is_completed) AS completedCount, " +
			"(SELECT COUNT(*) FROM comments c WHERE c.commenter_id = u.id AND c.comment_type='NOVEL_REVIEW') AS reviewsCount, " +
			"(SELECT MAX(ul.last_read_at) FROM user_library ul WHERE ul.user_id = u.id) AS lastActivity, " +
			"(SELECT IF(u.is_oauth2_authenticated = 1, 'Gmail', 'Email and password')) AS registerType " +
			"FROM user u WHERE u.email = :email"
	)
	Optional<UserInfoProjection> findUserInfoById(String email);
}