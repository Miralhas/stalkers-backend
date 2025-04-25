package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels")
public class NovelController {

	private final NovelMapper novelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Novel createNovel(@RequestBody @Valid NovelInput novelInput) {
		var novel = novelMapper.fromInput(novelInput);
		return novel;
	}
}
