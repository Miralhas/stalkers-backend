package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterDTO;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.input.BulkChaptersInput;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.NovelService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels/{novelSlug}/chapters")
public class ChapterController {

	private final ChapterService chapterService;
	private final ChapterMapper chapterMapper;
	private final NovelService novelService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<ChapterSummaryDTO> getAllChaptersByNovelId(
			@PathVariable String novelSlug,
			@PageableDefault(size = 100, sort = {"createdAt", "id"}, direction = Sort.Direction.ASC) Pageable pageable
	) {
		var chaptersPage = chapterService.findAllByNovelSlug(novelSlug, pageable);
		List<ChapterSummaryDTO> chapterSummaryDTOs = chaptersPage.getContent();
		var chaptersSummaryDTOsPage = new PageImpl<>(chapterSummaryDTOs, pageable, chaptersPage.getTotalElements());
		return new PageDTO<>(chaptersSummaryDTOsPage);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{chapterSlug}")
	public ChapterDTO getChapterBySlug(@PathVariable String chapterSlug, @PathVariable String novelSlug) {
		var chapter = chapterService.findChapterBySlugOrExceptionCacheable(chapterSlug);
		return chapterMapper.toResponse(chapter);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ChapterDTO create(@RequestBody @Valid ChapterInput chapterInput, @PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var chapter = chapterService.save(novel, chapterMapper.fromInput(chapterInput));
		return chapterMapper.toResponse(chapter);
	}

	@PostMapping("/save-bulk")
	@ResponseStatus(HttpStatus.CREATED)
	public void createBulk(@RequestBody @Valid BulkChaptersInput chapterInputs, @PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var chapters = chapterMapper.fromInputCollection(chapterInputs.chapters());
		chapterService.saveBulk(novel, chapters);
	}

	@PutMapping("/{chapterSlug}")
	@ResponseStatus(HttpStatus.OK)
	public ChapterDTO update(
			@PathVariable String novelSlug,
			@PathVariable String chapterSlug,
			@RequestBody @Valid ChapterInput chapterInput
	) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var chapter = chapterService.findChapterBySlug(chapterSlug);
		chapter = chapterService.update(chapter, chapterInput, novel);
		return chapterMapper.toResponse(chapter);
	}

	@DeleteMapping("/{chapterSlug}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String novelSlug, @PathVariable String chapterSlug) {
		chapterService.delete(chapterSlug, novelSlug);
	}

}
