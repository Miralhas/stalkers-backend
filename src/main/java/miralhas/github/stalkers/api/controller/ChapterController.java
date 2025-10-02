package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterDTO;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.CommentDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.input.BulkChaptersInput;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.api.dto.input.CommentInput;
import miralhas.github.stalkers.api.dto.input.UpdateCommentInput;
import miralhas.github.stalkers.api.dto_mapper.ChapterMapper;
import miralhas.github.stalkers.api.dto_mapper.CommentMapper;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.service.ChapterService;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.ReviewService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels/{novelSlug}/chapters")
public class ChapterController {

	private final ChapterService chapterService;
	private final ChapterMapper chapterMapper;
	private final NovelService novelService;
	private final ReviewService reviewService;
	private final CommentMapper commentMapper;

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
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@ResponseStatus(HttpStatus.CREATED)
	public ChapterDTO create(@RequestBody @Valid ChapterInput chapterInput, @PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var chapter = chapterService.save(novel, chapterMapper.fromInput(chapterInput));
		return chapterMapper.toResponse(chapter);
	}

	@PostMapping("/save-bulk")
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@ResponseStatus(HttpStatus.CREATED)
	public void createBulk(@RequestBody @Valid BulkChaptersInput chapterInputs, @PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var chapters = chapterMapper.fromInputCollection(chapterInputs.chapters());
		chapterService.saveBulk(novel, chapters);
	}

	@PutMapping("/update-bulk")
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateBulk(
			@RequestBody @Valid BulkChaptersInput chapterInputs, @PathVariable String novelSlug
	) {
		var novel = novelService.findBySlugOrException(novelSlug);
		chapterService.updateBulk(chapterInputs, novel);

	}

	@PutMapping("/{chapterSlug}")
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
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
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String novelSlug, @PathVariable String chapterSlug) {
		chapterService.delete(chapterSlug, novelSlug);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{chapterSlug}/reviews")
	public PageDTO<CommentDTO> chapterReviews(
			@PathVariable String novelSlug,
			@PathVariable String chapterSlug,
			@PageableDefault(size = 5, sort = {"voteCount", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return reviewService.findChapterReviewsBySlug(chapterSlug, pageable);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{chapterSlug}/reviews")
	public CommentDTO addReview(
			@PathVariable String novelSlug,
			@PathVariable String chapterSlug,
			@RequestBody @Valid CommentInput input
	) {
		Comment review = reviewService.addChapterReview(input, chapterSlug);
		return commentMapper.toResponse(review);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{chapterSlug}/reviews/{reviewId}")
	public CommentDTO updateReview(
			@RequestBody @Valid UpdateCommentInput input,
			@PathVariable String novelSlug,
			@PathVariable String chapterSlug,
			@PathVariable Long reviewId
	) {
		Comment updatedComment = reviewService.updateChapterComment(input, reviewId, chapterSlug);
		return commentMapper.toResponse(updatedComment);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{chapterSlug}/reviews/{reviewId}")
	public void deleteReview(
			@PathVariable String novelSlug,
			@PathVariable String chapterSlug,
			@PathVariable Long reviewId
	) {
		reviewService.deleteChapterComment(reviewId, chapterSlug);
	}


}
