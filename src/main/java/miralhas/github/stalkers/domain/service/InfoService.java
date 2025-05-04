package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.filter.TagFilter;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.repository.GenreRepository;
import miralhas.github.stalkers.domain.repository.TagRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Cacheable(cacheNames = "tags.list")
	public Page<Tag> findAllTags(TagFilter filter, Pageable pageable) {
		if (StringUtils.hasText(filter.firstLetter())) {
			return tagRepository.findAllTagsWithTheFirstLetter(filter.firstLetter(), pageable);
		}
		return tagRepository.findAll(pageable);
	}

	@Cacheable("genres.list")
	public List<Genre> findAllGenres() {
		return genreRepository.findAll();
	}


}
