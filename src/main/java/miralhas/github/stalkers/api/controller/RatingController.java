package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.RatingInput;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{novelSlug}/rating")
public class RatingController {

	private final NovelService novelService;
	private final RatingService ratingService;

	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void rateNovel(@PathVariable String novelSlug, @RequestBody @Valid RatingInput input) {
		var novel = novelService.findBySlugOrException(novelSlug);
		ratingService.createRating(input, novel);
	}

}
