package miralhas.github.stalkers.domain.service.interfaces;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.GenreNotFoundException;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.repository.GenreRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

	private final GenreRepository genreRepository;
	private final ErrorMessages errorMessages;

	@Cacheable("genres.detail")
	public Genre findGenreByNameOrException(String name){
		return genreRepository.findByName(name).orElseThrow(() -> new GenreNotFoundException(
				errorMessages.get("genre.notFound.name", name)
		));
	}
}
