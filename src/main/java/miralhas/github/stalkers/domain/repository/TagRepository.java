package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.novel.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
	@Query(value = "from Tag t where t.name = :name")
	Optional<Tag> findByName(String name);

	@Query(value = "from Tag t where t.slug = :slug")
	Optional<Tag> findBySlug(String slug);

	@Query("from Tag t where t.name like concat(:character, '%')")
	Page<Tag> findAllTagsWithTheFirstLetter(String character, Pageable pageable);

	@Query("from Tag t where t.name like concat('%', :character, '%')")
	Page<Tag> findAllTagsByNameContaining(String character, Pageable pageable);
}