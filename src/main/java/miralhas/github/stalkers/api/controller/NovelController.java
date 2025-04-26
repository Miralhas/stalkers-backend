package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.NovelDTO;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.service.NovelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels")
public class NovelController {

	private final NovelMapper novelMapper;
	private final NovelService novelService;

	@GetMapping
	public List<NovelDTO> findAll() {
		return novelMapper.toCollectionResponse(novelService.findAll());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Novel createNovel(@RequestBody @Valid NovelInput novelInput) {
		return novelService.save(novelInput);
	}
}
