package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.NovelDTO;
import miralhas.github.stalkers.api.dto.NovelSummaryDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.NovelFilter;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.api.dto.input.UpdateNovelInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.exception.NovelAlreadyExistsException;
import miralhas.github.stalkers.domain.exception.NovelNotFoundException;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.interfaces.GenreService;
import miralhas.github.stalkers.domain.utils.CacheManagerUtils;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NovelService {

	private final NovelRepository novelRepository;
	private final ErrorMessages errorMessages;
	private final NovelMapper novelMapper;
	private final ChapterMapper chapterMapper;
	private final ChapterService chapterService;
	private final ImageService imageService;
	private final TagsService tagsService;
	private final GenreService genreService;
	private final CacheManagerUtils cacheManagerUtils;

	@Cacheable(cacheNames = "novels.list", unless = "#result.getContent().isEmpty()")
	public Page<NovelSummaryDTO> findAll(Pageable pageable, NovelFilter filter) {
		Page<Novel> novelsPage = novelRepository.findAll(filter.toSpecification(), pageable);
		List<NovelSummaryDTO> novelSummaryDTOS = novelMapper.toSummaryCollectionResponse(novelsPage.getContent());
		return new PageImpl<>(novelSummaryDTOS, pageable, novelsPage.getTotalElements());
	}

	@Cacheable(cacheNames = "novels.slugs.list", unless = "#result.results.empty")
	public PageDTO<String> findAllNovelSlugs(Pageable pageable) {
		var slugsPage = novelRepository.findAllNovelSlugs(pageable);
		return new PageDTO<>(slugsPage);
	}

	@Cacheable(cacheNames = "novels.detail")
	public NovelDTO findBySlugOrExceptionCacheable(String slug) {
		return novelMapper.toResponse(findBySlugWithTagsAndGenresOrException(slug));
	}

	public Novel findBySlugOrException(String slug) {
		return novelRepository.findBySlug(slug).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("novel.notFound.slug", slug)
		));
	}

	public Novel findBySlugWithTagsAndGenresOrException(String slug) {
		return novelRepository.findBySlugWithTagsAndGenres(slug).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("novel.notFound.slug", slug)
		));
	}

	public Novel findByIdOrException(Long id) {
		return novelRepository.findById(id).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("novel.notFound.id", id)
		));
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public Novel save(NovelInput novelInput) {
		var novel = novelMapper.fromInput(novelInput);
		novel.generateSlug();
		performValidations(novel);

		var chapters = chapterMapper.fromInputCollection(novelInput.chapters());

		novel = novelRepository.save(novel);
		chapterService.saveBulk(novel, chapters);

		return novel;
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public Novel update(UpdateNovelInput input, Novel novel) {
		var initialNovelSlug = novel.getSlug();
		var initialNovelTitle = novel.getTitle();
		novel = novelMapper.update(input, novel);
		novel.generateSlug();

		shouldValidateOnUpdate(novel, input, initialNovelTitle);

		novel = novelRepository.save(novel);
		cacheManagerUtils.evictSingleEntry("novels.detail::%s".formatted(initialNovelSlug));
		return novel;
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public void delete(String novelSlug) {
		var novel = findBySlugOrException(novelSlug);
		// soft delete
		novel.setIsHidden(true);
		novelRepository.save(novel);
		cacheManagerUtils.evictSingleEntry("novels.detail::%s".formatted(novelSlug));
	}

	@Transactional
	public Image saveImage(Novel novel, Image image, InputStream inputStream) {
		if (novel.hasImage()) {
			var novelImage = novel.getImage();
			novel.setImage(null);
			novelRepository.saveAndFlush(novel);
			imageService.delete(novelImage);
		}

		image = imageService.save(image, inputStream);
		novel.setImage(image);
		novelRepository.save(novel);
		return imageService.getImageJsonOrException(image.getId());
	}

	@Transactional
	public void deleteImage(Novel novel) {
		imageService.delete(novel.getImage());
		novel.setImage(null);
		novelRepository.save(novel);
	}

	private void shouldValidateOnUpdate(Novel novel, UpdateNovelInput input, String initialNovelTitle) {
		var shouldValidateSlug = !initialNovelTitle.equals(novel.getTitle());
		var shouldValidateTags = input.tags() != null;
		var shouldValidateGenres = input.genres() != null;
		// if the update is changing the novel title, then a validation is needed (change slug)
		if (shouldValidateSlug) {
			validateSlug(novel.getSlug());
		}

		if (shouldValidateTags) {
			validateTags(novel);
		}

		if (shouldValidateGenres) {
			validateGenres(novel);
		}
	}

	private void performValidations(Novel novel) {
		validateSlug(novel.getSlug());
		validateTagsAndGenres(novel);
	}

	private void validateTagsAndGenres(Novel novel) {
		validateGenres(novel);
		validateTags(novel);
	}

	private void validateSlug(String slug) {
		var exists = novelRepository.checkIfSlugAlreadyExists(slug);
		if (exists) throw new NovelAlreadyExistsException(
				errorMessages.get("novel.alreadyExists.slug", slug)
		);
	}

	private void validateTags(Novel novel) {
		var tags = novel.getTags()
				.stream()
				.map(t -> tagsService.findTagByNameOrException(t.getName()))
				.collect(Collectors.toSet());
		novel.setTags(tags);
	}

	private void validateGenres(Novel novel) {
		var genres = novel.getGenres()
				.stream()
				.map(t -> genreService.findGenreByNameOrException(t.getName()))
				.collect(Collectors.toSet());
		novel.setGenres(genres);
	}

}
