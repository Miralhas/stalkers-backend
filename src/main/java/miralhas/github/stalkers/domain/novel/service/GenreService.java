package miralhas.github.stalkers.domain.novel.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.novel.exception.GenreNotFoundException;
import miralhas.github.stalkers.domain.novel.model.Genre;
import miralhas.github.stalkers.domain.novel.repository.GenreRepository;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
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

	@Cacheable("genres.detail")
	public Genre findGenreBySlugOrException(String slug){
		return genreRepository.findBySlug(slug).orElseThrow(() -> new GenreNotFoundException(
				errorMessages.get("genre.notFound.slug", slug)
		));
	}
}
