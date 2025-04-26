package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.exception.NovelAlreadyExistsException;
import miralhas.github.stalkers.domain.exception.NovelNotFoundException;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NovelService {


	private final NovelRepository novelRepository;
	private final ErrorMessages errorMessages;
	private final NovelMapper novelMapper;
	private final ChapterMapper chapterMapper;
	private final ChapterService chapterService;

	@Cacheable(cacheNames = "novels.list")
	public List<Novel> findAll() {
		return novelRepository.findAll();
	}

	@Cacheable(cacheNames = "novels.detail")
	public Novel findByIdentifierOrExceptionCacheable(String chapterIdentifier) {
		return null;
	}

	public Novel findByIdOrException(Long id) {
		return novelRepository.findById(id).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("novel.notFound.id", id)
		));
	}

	public Novel findBySlugOrException(String slug) {
		return novelRepository.findBySlug(slug).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("novel.notFound.slug", slug)
		));
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public Novel save(NovelInput novelInput) {
		var novel = novelMapper.fromInput(novelInput);
		var chapters = chapterMapper.fromInputCollection(novelInput.chapters());
		novel.generateSlug();
		validateSlug(novel.getSlug());

		novel = novelRepository.save(novel);
		chapterService.saveBulk(novel, chapters);

		return novel;
	}

	private void validateSlug(String slug) {
		var exists = novelRepository.checkIfSlugAlreadyExists(slug);
		if (exists) throw new NovelAlreadyExistsException(
				errorMessages.get("novel.alreadyExists.slug", slug)
		);
	}

}
