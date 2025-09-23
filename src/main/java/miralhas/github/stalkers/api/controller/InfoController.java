package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.LatestChapterDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.TagFilter;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.InfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfoController {

	private final InfoService infoService;
	private final ChapterService chapterService;

	@GetMapping("/latest-chapters")
	public PageDTO<LatestChapterDTO> getLatestChaptersDTO(
			@PageableDefault(size = 75, sort = {"created_at", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return chapterService.getLatestChaptersDTO(pageable);
	}

	@GetMapping("/tags")
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<Tag> getAllTags(
			@PageableDefault(size = 100, sort = {"name", "id"}, direction = Sort.Direction.ASC) Pageable pageable,
			@Valid TagFilter filter
	) {
		var tagsPage = infoService.findAllTags(filter, pageable);
		return new PageDTO<>(tagsPage);
	}

	@GetMapping("/genres")
	@ResponseStatus(HttpStatus.OK)
	public List<Genre> getAllGenres() {
		return infoService.findAllGenres();
	}
}
