package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.SeriesDTO;
import miralhas.github.stalkers.api.dto.input.SeriesInput;
import miralhas.github.stalkers.api.dto.input.SeriesSlugsInput;
import miralhas.github.stalkers.api.dto.input.UpdateSeriesInput;
import miralhas.github.stalkers.api.dto_mapper.SeriesMapper;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.SeriesService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Series")
@RequiredArgsConstructor
@RequestMapping("/api/series")
public class SeriesController {

	private final SeriesService seriesService;
	private final SeriesMapper seriesMapper;
	private final NovelService novelService;

	@GetMapping
	public List<SeriesDTO> findAll() {
		return seriesService.findAll().stream().map(seriesMapper::toResponse).toList();
	}

	@GetMapping("/{slug}")
	public SeriesDTO find(@PathVariable String slug) {
		return seriesMapper.toResponse(seriesService.findBySlugOrException(slug));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public SeriesDTO createSeries(@RequestBody @Valid SeriesInput input) {
		var books = input.slugs().stream().map(novelService::findBySlugOrException).toList();
		return seriesMapper.toResponse(seriesService.save(input, books));
	}

	@PutMapping("/{slug}")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public SeriesDTO updateSeries(@RequestBody @Valid UpdateSeriesInput input, @PathVariable String slug) {
		return seriesMapper.toResponse(seriesService.updateSeries(slug, input));
	}

	@DeleteMapping("/{slug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void deleteSeries(@PathVariable String slug) {
		seriesService.delete(slug);
	}

	@PutMapping("/{slug}/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void addAllBooks(@PathVariable String slug, @RequestBody @Valid SeriesSlugsInput input) {
		var series = seriesService.findBySlugOrException(slug);
		var books = input.slugs().stream().map(novelService::findBySlugOrException).toList();
		seriesService.addAll(series, books);
	}

	@PutMapping("/{slug}/{novelSlug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void addBook(@PathVariable String slug, @PathVariable String novelSlug) {
		var series = seriesService.findBySlugOrException(slug);
		var book = novelService.findBySlugOrException(novelSlug);
		seriesService.add(series, book);
	}

	@DeleteMapping("/{slug}/{novelSlug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void removeBook(@PathVariable String slug, @PathVariable String novelSlug) {
		var series = seriesService.findBySlugOrException(slug);
		var book = novelService.findBySlugOrException(novelSlug);
		seriesService.removeBook(series, book);
	}
}
