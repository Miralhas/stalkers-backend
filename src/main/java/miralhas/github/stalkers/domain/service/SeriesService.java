package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.SeriesInput;
import miralhas.github.stalkers.api.dto.input.UpdateSeriesInput;
import miralhas.github.stalkers.api.dto_mapper.SeriesMapper;
import miralhas.github.stalkers.domain.exception.BookAlreadyBelongsException;
import miralhas.github.stalkers.domain.exception.SeriesAlreadyExistsException;
import miralhas.github.stalkers.domain.exception.SeriesNotFoundException;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.series.Series;
import miralhas.github.stalkers.domain.repository.SeriesRepository;
import miralhas.github.stalkers.domain.utils.CacheManagerUtils;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeriesService {
	private final SeriesRepository repository;
	private final ErrorMessages errorMessages;
	private final SeriesMapper seriesMapper;
	private final NovelService novelService;
	private final CacheManagerUtils cacheManagerUtils;

	public Series findBySlugOrException(String slug) {
		return repository.findBySlug(slug).orElseThrow(() -> new SeriesNotFoundException(
				errorMessages.get("series.notFound.slug", slug))
		);
	}

	public List<Series> findAll() {
		return repository.findAll();
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public Series save(SeriesInput input) {
		var series = seriesMapper.fromInput(input);
		series.generateSlug();
		this.validateSlug(series.getSlug());

		var books = input.slugs().stream().map(novelService::findBySlugOrException).toList();

		validateBooks(books);
		series.addAllBooks(books);

		return repository.save(series);
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public Series updateSeries(String slug, UpdateSeriesInput input) {
		var series = findBySlugOrException(slug);
		seriesMapper.update(input, series);
		series.generateSlug();
		validateSlug(series.getSlug());
		return repository.save(series);
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public void delete(String slug) {
		var series = findBySlugOrException(slug);
		series.removeAllBooks();
		repository.delete(series);
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public void add(Series series, String slug) {
		var novel = novelService.findBySlugOrException(slug);
		validateBooks(List.of(novel));
		series.addBook(novel);
		repository.save(series);
		cacheManagerUtils.evictSingleEntry("novels.detail::%s".formatted(novel.getSlug()));
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public void addAll(Series series, List<String> slugs) {
		var books = slugs.stream().map(novelService::findBySlugOrException).toList();
		validateBooks(books);
		series.addAllBooks(books);
		repository.save(series);
		books.forEach(novel -> cacheManagerUtils.evictSingleEntry(
				"novels.detail::%s".formatted(novel.getSlug()))
		);
	}

	@Transactional
	@CacheEvict(cacheNames = "novels.list", allEntries = true)
	public void removeBook(Series series, String slug) {
		Novel novel = novelService.findBySlugOrException(slug);
		series.removeBook(novel);
		repository.save(series);
		cacheManagerUtils.evictSingleEntry("novels.detail::%s".formatted(novel.getSlug()));
	}

	private void validateBooks(List<Novel> books) {
		Map<String, String> errors = new HashMap<>();
		books.stream().filter(Novel::hasSeries)
				.forEach(b -> errors.put(
						b.capitalizedTitle(),
						errorMessages.get("series.book.alreadyBelongs", b.getSeriesTitle())
				));

		if (!errors.isEmpty()) {
			throw new BookAlreadyBelongsException(errorMessages.get("series.book.invalid"), errors);
		}
	}

	private void validateSlug(String slug) {
		var exists = repository.checkIfSlugAlreadyExists(slug);
		if (exists) throw new SeriesAlreadyExistsException(
				errorMessages.get("series.alreadyExists.slug", slug)
		);
	}
}
