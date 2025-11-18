package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthorDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.TagFilter;
import miralhas.github.stalkers.domain.exception.AuthorNotFoundException;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.repository.GenreRepository;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.repository.TagRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {

	private final TagRepository tagRepository;
	private final GenreRepository genreRepository;
	private final NovelRepository novelRepository;
	private final ErrorMessages errorMessages;

	@Cacheable(cacheNames = "tags.list")
	public Page<Tag> findAllTags(TagFilter filter, Pageable pageable) {

		if (StringUtils.hasText(filter.q())) {
			return tagRepository.findAllTagsByNameContaining(filter.q(), pageable);
		} else if(StringUtils.hasText(filter.firstLetter())) {
			return tagRepository.findAllTagsWithTheFirstLetter(filter.firstLetter(), pageable);
		}

		return tagRepository.findAll(pageable);
	}

	@Cacheable("genres.list")
	public List<Genre> findAllGenres() {
		return genreRepository.findAll();
	}

	@Cacheable("authors.list")
	public PageDTO<AuthorDTO> getAllAuthors(Pageable pageable, String authorQuery) {

		if (StringUtils.hasText(authorQuery)) {
			Page<AuthorDTO> allAuthors = novelRepository.findAllAuthorsWithName(pageable, authorQuery);
			return new PageDTO<>(allAuthors);
		}

		Page<AuthorDTO> allAuthors = novelRepository.findAllAuthors(pageable);
		return new PageDTO<>(allAuthors);
	}

	@Cacheable("authors.detail")
	public AuthorDTO findAuthorByName(String name) {
		return novelRepository.findAuthorByName(name).orElseThrow(() -> new AuthorNotFoundException(
				errorMessages.get("author.notFound", name)
		));
	}
}
