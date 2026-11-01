package miralhas.github.stalkers.api.info.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.novel.dto.AuthorDTO;
import miralhas.github.stalkers.api.chapter.dto.LatestChapterDTO;
import miralhas.github.stalkers.api.novel.dto.NovelInfoDTO;
import miralhas.github.stalkers.api.shared.dto.PageDTO;
import miralhas.github.stalkers.api.novel.dto.filter.AuthorFilter;
import miralhas.github.stalkers.api.novel.dto.filter.NovelFilter;
import miralhas.github.stalkers.api.novel.dto.filter.TagFilter;
import miralhas.github.stalkers.api.novel.mapper.NovelMapper;
import miralhas.github.stalkers.domain.novel.model.Genre;
import miralhas.github.stalkers.domain.novel.model.Tag;
import miralhas.github.stalkers.domain.chapter.repository.ChapterRepository;
import miralhas.github.stalkers.domain.novel.repository.NovelRepository;
import miralhas.github.stalkers.domain.chapter.service.ChapterService;
import miralhas.github.stalkers.domain.info.service.InfoService;
import miralhas.github.stalkers.domain.novel.service.TagsService;
import miralhas.github.stalkers.domain.novel.service.GenreService;
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
			@PageableDefault(size = 50, sort = {"novelsCount", "name"}, direction = Sort.Direction.DESC) Pageable pageable,
			AuthorFilter filter
	) {
		return infoService.getAllAuthors(pageable, filter.getQ());
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

	@GetMapping("/genres/{slug}")
	@ResponseStatus(HttpStatus.OK)
	public Genre getGenreByName(@PathVariable String slug) {
		return genreService.findGenreBySlugOrException(slug);
	}

	@GetMapping("/tags/{slug}")
	@ResponseStatus(HttpStatus.OK)
	public Tag getTagByName(@PathVariable String slug) {
		return tagsService.findTagBySlugOrException(slug);
	}
}
