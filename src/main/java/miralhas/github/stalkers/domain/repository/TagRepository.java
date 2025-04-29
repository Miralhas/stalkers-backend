package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.novel.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
	@Query(value = "from Tag t where t.name = :name")
	Optional<Tag> findByName(String name);
}