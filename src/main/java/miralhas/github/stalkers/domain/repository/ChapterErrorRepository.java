package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.requests.ChapterError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChapterErrorRepository extends JpaRepository<ChapterError, Long> {

  @Query("from ChapterError ce WHERE ce.slug = :slug")
  Optional<ChapterError> findBySlug(String slug);

  @Query(nativeQuery = true,
          value = "SELECT IF(EXISTS(SELECT 1 FROM chapter_error a WHERE a.slug = :slug), 'true', 'false')"
  )
  boolean checkIfSlugAlreadyExists(String slug);

}