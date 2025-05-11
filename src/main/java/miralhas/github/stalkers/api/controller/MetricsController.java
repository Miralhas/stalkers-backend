package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.RatingInput;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metrics/{novelSlug}")
public class MetricsController {

	private final NovelService novelService;
	private final MetricsService metricsService;

	@PostMapping("/rating")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void rateNovel(@PathVariable String novelSlug, @RequestBody @Valid RatingInput input) {
		var novel = novelService.findBySlugOrException(novelSlug);
		metricsService.createRating(input, novel);
	}

	@PutMapping("/view")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void viewNovel(@PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		metricsService.updateNovelViewCount(novel);
	}

}
