package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.novel.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query(value = "from Genre g where g.name = :name")
	Optional<Genre> findByName(String name);
}