package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthorDTO;
import miralhas.github.stalkers.api.dto.LatestChapterDTO;
import miralhas.github.stalkers.api.dto.NovelInfoDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.NovelFilter;
import miralhas.github.stalkers.api.dto.filter.TagFilter;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.repository.ChapterRepository;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.InfoService;
import miralhas.github.stalkers.domain.service.TagsService;
import miralhas.github.stalkers.domain.service.interfaces.GenreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfoController {

	private final InfoService infoService;
	private final ChapterService chapterService;
	private final NovelRepository novelRepository;
	private final NovelMapper novelMapper;
	private final GenreService genreService;
	private final TagsService tagsService;
	private final ChapterRepository chapterRepository;

	@GetMapping("/latest-ids")
	public List<Long> getLatestIDS(
			@PageableDefault(size = 100, sort = {"created_at", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return chapterRepository.findAllLastestChaptersIDS();
	}

	@GetMapping("/latest-chapters")
	public PageDTO<LatestChapterDTO> getLatestChaptersDTO(
			@PageableDefault(size = 100, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return chapterService.getLatestChaptersDTO(pageable);
	}

	@GetMapping("/authors")
	public PageDTO<AuthorDTO> getAllAuthors(
			@PageableDefault(size = 50, sort = {"novelsCount", "name"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return infoService.getAllAuthors(pageable);
	}
	@GetMapping("/authors/{name}")
	public AuthorDTO getAuthorByName(@PathVariable String name) {
		return infoService.findAuthorByName(name);
	}

	@GetMapping("/info/novels")
	public List<NovelInfoDTO> getAllNovelsInfo(@Valid NovelFilter filter) {
		return novelRepository.findAll(filter.toSpecification()).stream().map(novelMapper::toInfoResponse).toList();
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

	@GetMapping("/genres/{name}")
	@ResponseStatus(HttpStatus.OK)
	public Genre getGenreByName(@PathVariable String name) {
		return genreService.findGenreByNameOrException(name);
	}

	@GetMapping("/tags/{name}")
	@ResponseStatus(HttpStatus.OK)
	public Tag getTagByName(@PathVariable String name) {
		return tagsService.findTagByNameOrException(name);
	}
}
