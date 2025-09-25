package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.novel.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query(value = "from Genre g where g.name = :name")
	Optional<Genre> findByName(String name);

	@Query(value = "from Genre g where g.slug = :slug")
	Optional<Genre> findBySlug(String slug);

	@Query(nativeQuery = true, value = """
			SELECT DISTINCT n.slug FROM genre g
			LEFT JOIN novel_genres ng ON g.id = ng.genre_id
			LEFT JOIN novel n ON ng.novel_id = n.id
			WHERE g.slug = :genreSlug LIMIT :limit;
			""")
	List<String> findGenreNovels(String genreSlug, int limit);


}