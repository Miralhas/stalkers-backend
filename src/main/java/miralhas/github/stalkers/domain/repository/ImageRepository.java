package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
	@Query("from Image i where i.fileName = :fileName")
	Optional<Image> findImageByFileName(String fileName);
}