package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.SeriesDTO;
import miralhas.github.stalkers.api.dto.input.SeriesInput;
import miralhas.github.stalkers.api.dto.input.SeriesSlugsInput;
import miralhas.github.stalkers.api.dto.input.UpdateSeriesInput;
import miralhas.github.stalkers.api.dto_mapper.SeriesMapper;
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

	@GetMapping
	public List<SeriesDTO> findAll() {
		return seriesService.findAll().stream().map(seriesMapper::toResponse).toList();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public SeriesDTO createSeries(@RequestBody @Valid SeriesInput input) {
		return seriesMapper.toResponse(seriesService.save(input));
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
		seriesService.addAll(series, input.slugs().stream().toList());
	}

	@PutMapping("/{slug}/{novelSlug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void addBook(@PathVariable String slug, @PathVariable String novelSlug) {
		var series = seriesService.findBySlugOrException(slug);
		seriesService.add(series, novelSlug);
	}

	@DeleteMapping("/{slug}/{novelSlug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void removeBook(@PathVariable String slug, @PathVariable String novelSlug) {
		var series = seriesService.findBySlugOrException(slug);
		seriesService.removeBook(series, novelSlug);
	}
}
