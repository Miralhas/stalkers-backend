package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
	@Query("from Image i where i.fileName = :fileName")
	Optional<Image> findImageByFileName(String fileName);

	@Modifying
	@Query(value = "delete from Image i where i.fileName = :fileName")
	void deleteImageByFileName(String fileName);

	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Image i " +
			"LEFT JOIN User u ON u.image.id = i.id " +
			"WHERE u.id = :userId")
	boolean checkIfUserHasImage(Long userId);
}