package miralhas.github.stalkers.domain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.LatestChapterDTO;
import miralhas.github.stalkers.api.dto.LatestChaptersProjection;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.ChaptersRange;
import miralhas.github.stalkers.api.dto.input.BulkChaptersInput;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.domain.exception.ChapterNotFoundException;
import miralhas.github.stalkers.domain.exception.NovelAlreadyExistsException;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.ChapterRepository;
import miralhas.github.stalkers.domain.utils.CacheManagerUtils;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	private final CacheManagerUtils cacheManager;
	private final NotificationService notificationService;

	@PersistenceContext
	private final EntityManager entityManager;

	@Cacheable(cacheNames = "latest.list", unless = "#result.getResults().isEmpty()")
	public PageDTO<LatestChapterDTO> getLatestChaptersDTO(Pageable pageable) {
		var latestProjections = chapterRepository.findAllLatestChaptersDTO(pageable);

		var latestDTO = latestProjections.getContent().stream()
				.map(LatestChaptersProjection::getLatestChaptersDTO).toList();

		var page = new PageImpl<>(latestDTO, pageable, latestProjections.getTotalElements());
		return new PageDTO<>(page);
	}

	@Cacheable(value = "chapters.list", unless = "#result.getContent().isEmpty()", keyGenerator = "novelChaptersKeyGenerator")
	public Page<ChapterSummaryDTO> findAllByNovelSlug(String novelSlug, Pageable pageable) {
		return chapterRepository.findAllByNovelSlug(novelSlug, pageable);
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

	public Chapter findByNovelSlugAndChapterNumber(String novelSlug, Long number) {
		return chapterRepository.findByNovelSlugAndChapterNumber(novelSlug, number)
				.orElseThrow(() -> new ChapterNotFoundException(
						errorMessages.get("chapter.notFound.novelSlug.number", novelSlug, number)
				));
	}

	public Chapter findByIdOrException(Long id) {
		return chapterRepository.findById(id).orElseThrow(() -> new ChapterNotFoundException(
				errorMessages.get("chapter.notFound.id", id)
		));
	}

	public List<Chapter> getChaptersInRange(ChaptersRange range, Novel novel) {
		int min = range.getFirstValue().intValue();
		int max = range.getSecondValue().intValue();
		return chapterRepository.chaptersBetweenRange(novel.getSlug(), min, max);
	}

	// Cache Breakdown:
	// 2. Evict cache for novels.detail (cache for a single novel). This entry is deleted because the object cached
	// 		in novels.detail contains 2 fields that are related to chapters (firstChapter and lastChapter).
	// 3. Evict all entries to chapters.list of the novel that is receiving new chapter/s
	// 		(cache containing all chapter of a single novel)

	@Transactional
	@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")
	public Chapter save(Novel novel, Chapter chapter) {
		setChapterPropertiesAndValidations(chapter, novel);
		// evict only chapters.list of the novel that is receiving new chapters
		Chapter saved = chapterRepository.save(chapter);
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
		notificationService.sendNewChapterNotification(novel, saved);
		return saved;
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")
	public void saveBulk(Novel novel, List<Chapter> chapters) {
		chapters.forEach(chapter -> setChapterPropertiesAndValidations(chapter, novel));
		chapters = chapterRepository.saveAll(chapters);
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
		notificationService.sendNewChapterNotification(novel, chapters.getFirst());
	}

	// Cache Breakdown:
	// 1. Evict cache for novels.detail (cache for a single novel). This entry is deleted because the object cached
	// 		in novels.detail contains 2 fields that are related to chapters (firstChapter and lastChapter), so it needs
	// 		to be updated whenever a chapter of that novel is saved/updated/deleted.
	// 2. Update cache for the entry that is being updated
	// 3. Evict all entries to chapters.list of that novel (cache containing all chapter of a single novel)

	@Transactional
	@Caching(
			evict = {@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")},
			put = {@CachePut(cacheNames = "chapters.detail", key = "#result.slug")}
	)
	public Chapter update(Chapter chapter, ChapterInput chapterInput, Novel novel) {
		var initialChapterNumber = chapter.getNumber();
		chapterMapper.update(chapterInput, chapter);

		// if the update is changing the chapter number, then a validation is needed (change slug)
		if (!initialChapterNumber.equals(chapterInput.number())) {
			setChapterPropertiesAndValidations(chapter, novel);
		}

		Chapter saved = chapterRepository.save(chapter);
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
		return saved;
	}

	@Transactional
	@Caching(
			evict = {
					@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug"),
					@CacheEvict(cacheNames = "chapters.detail", allEntries = true),
			}
	)
	public void updateBulk(BulkChaptersInput chaptersInput, Novel novel) {
		var editedChapters = chaptersInput.chapters().stream().map(input -> {
			var chapter = findByNovelSlugAndChapterNumber(novel.getSlug(), input.number());
			chapterMapper.update(input, chapter);
			return chapter;
		}).toList();
		chapterRepository.saveAll(editedChapters);
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
	}

	@Transactional
	@Caching(
			evict = {
					@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug"),
					@CacheEvict(cacheNames = "chapters.detail", allEntries = true),
			}
	)
	public void updateBulkChuking(BulkChaptersInput chaptersInput, Novel novel) {
		var chunkSize = 500;
		for (int i = 0; i < chaptersInput.chapters().size(); i+= chunkSize) {
			var chunk = chaptersInput.chapters().subList(i, Math.min(i + chunkSize, chaptersInput.chapters().size()));
			updateChunk(chunk, novel);
		}
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
	}

	private void updateChunk(List<ChapterInput> chunk, Novel novel) {
		var editedChapters = chunk.stream().map(input -> {
			var chapter = findByNovelSlugAndChapterNumber(novel.getSlug(), input.number());
			chapterMapper.update(input, chapter);
			return chapter;
		}).toList();

		chapterRepository.saveAll(editedChapters);
		chapterRepository.flush(); // Force flush to avoid memory buildup
		entityManager.clear(); // Clear persistence context to free memory
	}

	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "novels.detail", key = "#novel.slug")
	})
	public void deleteBulk(ChaptersRange range, Novel novel){
		int min = range.getFirstValue().intValue();
		int max = range.getSecondValue().intValue();
		var chapters = chapterRepository.chaptersBetweenRange(novel.getSlug(), min, max);
		chapterRepository.deleteAll(chapters);
		cacheManager.evictNovelChaptersEntry(novel.getSlug());
	}

	// Cache Breakdown:
	// 1. Evict cache for chapters.detail (cache for a single chapter)
	// 2. Evict cache for novels.detail (cache for a single novel). This entry is deleted because the object cached
	// 		in novels.detail contains 2 fields that are related to chapters (firstChapter and lastChapter), so it needs
	// 		to be updated whenever a chapter of that novel is saved/updated/deleted.
	// 3. Evict all entries to chapters.list of that novel (cache containing all chapter of a single novel)

	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "chapters.detail", key = "#chapterSlug"),
			@CacheEvict(cacheNames = "novels.detail", key = "#novelSlug")
	})
	public void delete(String chapterSlug, String novelSlug) {
		var chapter = findChapterBySlug(chapterSlug);
		chapterRepository.delete(chapter);
//		chapterRepository.deleteBySlug(chapterSlug);
		cacheManager.evictNovelChaptersEntry(novelSlug);
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
