package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.RatingInput;
import miralhas.github.stalkers.domain.service.NovelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/{novelSlug}/rating")
public class RatingController {

	private final NovelService novelService;

	@PostMapping
	public void rateNovel(@PathVariable String novelSlug, @RequestBody @Valid RatingInput input) {
		var novel = novelService.findBySlugOrException(novelSlug);
		System.out.println(input);
	}

}
