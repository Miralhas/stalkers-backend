package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.domain.exception.ChapterNotFoundException;
import miralhas.github.stalkers.domain.exception.NovelAlreadyExistsException;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.ChapterRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterService {

	private final ErrorMessages errorMessages;
	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;

	@Cacheable(cacheNames = "chapters.list", unless = "#result.isEmpty()")
	public List<ChapterSummaryDTO> findAllByNovelSlug(String novelSlug) {
		return chapterRepository.findAllByNovelSlug(novelSlug);
	}

	@Cacheable(cacheNames = "chapters.detail")
	public Chapter findChapterBySlugOrExceptionCacheable(String chapterSlug) {
		return findChapterBySlug(chapterSlug);
	}

	public Chapter findChapterBySlug(String chapterSlug) {
		return chapterRepository.findBySlug(chapterSlug)
				.orElseThrow(() -> new ChapterNotFoundException(
						errorMessages.get("chapter.notFound.slug", chapterSlug)
				));
	}

	@Transactional
	@Caching(
			evict = {
					@CacheEvict(cacheNames = "chapters.list", key = "#novel.slug"),
					@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")
			}
	)
	public Chapter save(Novel novel, Chapter chapter) {
		setChapterPropertiesAndValidations(chapter, novel);
		return chapterRepository.save(chapter);
	}

	@Transactional
	@Caching(
			evict = {
					@CacheEvict(cacheNames = "chapters.list", key = "#novel.slug"),
					@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")
			}
	)
	public void saveBulk(Novel novel, List<Chapter> chapters) {
		chapters.forEach(chapter -> setChapterPropertiesAndValidations(chapter, novel));
		chapterRepository.saveAllAndFlush(chapters);
	}

	@Transactional
	@Caching(
			evict = {@CacheEvict(cacheNames = "chapters.list", key = "#novel.slug")},
			put = {@CachePut(cacheNames = "chapters.detail", key = "#result.slug")}
	)
	public Chapter update(Chapter chapter, ChapterInput chapterInput, Novel novel) {
		var initialChapterNumber = chapter.getNumber();
		chapterMapper.update(chapterInput, chapter);

		// caso o title do update seja diferente que o title anterior, fazer verificação (mudar slug)
		if (!initialChapterNumber.equals(chapterInput.number())) {
			setChapterPropertiesAndValidations(chapter, novel);
		}

		return chapterRepository.save(chapter);
	}

	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "chapters.list", key = "#novelSlug"),
			@CacheEvict(cacheNames = "chapters.detail", key = "#chapterSlug")
	})
	public void delete(String chapterSlug, String novelSlug) {
		chapterRepository.deleteBySlug(chapterSlug);
	}

	private void setChapterPropertiesAndValidations(Chapter chapter, Novel novel) {
		chapter.setNovel(novel);
		chapter.generateSlug(novel.getTitle());
		validateSlug(chapter.getSlug());
	}

	private void validateSlug(String slug) {
		var exists = chapterRepository.checkIfSlugAlreadyExists(slug);
		if (exists) throw new NovelAlreadyExistsException(
				errorMessages.get("chapter.alreadyExists.slug", slug)
		);
	}
}
