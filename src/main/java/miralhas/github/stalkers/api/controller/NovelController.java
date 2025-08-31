package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.*;
import miralhas.github.stalkers.api.dto.filter.NovelFilter;
import miralhas.github.stalkers.api.dto.input.*;
import miralhas.github.stalkers.api.dto_mapper.CommentMapper;
import miralhas.github.stalkers.api.dto_mapper.ImageMapper;
import miralhas.github.stalkers.api.dto_mapper.NovelMapper;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.service.ImageService;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.ReviewService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels")
public class NovelController {

	private final NovelMapper novelMapper;
	private final NovelService novelService;
	private final ImageMapper imageMapper;
	private final ImageService imageService;
	private final ReviewService reviewService;
	private final CommentMapper commentMapper;
	private final NovelRepository novelRepository;

	@GetMapping
	public PageDTO<NovelSummaryDTO> findAll(
			@PageableDefault(size = 10, sort = {"bayesianScore", "id"}, direction = Sort.Direction.ASC) Pageable pageable,
			@Valid NovelFilter filter
	) {
		var novelsPage = novelService.findAll(pageable, filter);
		return new PageDTO<>(novelsPage);
	}

	@GetMapping("/{novelSlug}")
	@ResponseStatus(HttpStatus.OK)
	public NovelDTO findBySlug(@PathVariable String novelSlug) {
		return novelService.findBySlugOrExceptionCacheable(novelSlug);
	}

	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{novelSlug}")
	public NovelDTO updateNovel(@PathVariable String novelSlug, @RequestBody @Valid UpdateNovelInput input) {
		var novel = novelService.findBySlugOrException(novelSlug);
		novel = novelService.update(input, novel);
		return novelMapper.toResponse(novel);
	}

	@DeleteMapping("/{novelSlug}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNovel(@PathVariable String novelSlug) {
		novelService.delete(novelSlug);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@ResponseStatus(HttpStatus.CREATED)
	public NovelDTO createNovel(@RequestBody @Valid NovelInput novelInput) {
		Novel novel = novelService.save(novelInput);
		return novelMapper.toResponse(novel);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{novelSlug}/image")
	public ResponseEntity<InputStreamResource> getImage(
			@PathVariable String novelSlug, @RequestHeader(name = "accept", defaultValue = "image/*") String acceptHeader
	) {
		var novel = novelService.findBySlugOrException(novelSlug);
		return imageService.getImage(novel.getImage());
	}

	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	@PutMapping(value = "/{novelSlug}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ImageDTO saveImage(@PathVariable String novelSlug, @Valid ImageInput imageInput) throws IOException {
		var image = imageMapper.fromInput(imageInput);
		var novel = novelService.findBySlugOrException(novelSlug);
		image = novelService.saveImage(novel, image, imageInput.fileInputStream());
		return imageMapper.toResponse(image);
	}

	@DeleteMapping("/{novelSlug}/image")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteImage(@PathVariable String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		novelService.deleteImage(novel);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{novelSlug}/reviews")
	public PageDTO<CommentDTO> novelReviews(
			@PathVariable String novelSlug,
			@PageableDefault(size = 10, sort = {"voteCount", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return reviewService.findNovelReviewsBySlug(novelSlug, pageable);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{novelSlug}/reviews")
	public CommentDTO addNovelReview(@PathVariable String novelSlug, @RequestBody @Valid CommentInput input) {
		Comment review = reviewService.addNovelReview(input, novelSlug);
		return commentMapper.toResponse(review);
	}


	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{novelSlug}/reviews/{reviewId}")
	public CommentDTO updateReview(
			@RequestBody @Valid UpdateCommentInput input,
			@PathVariable String novelSlug,
			@PathVariable Long reviewId
	) {
		Comment updatedComment = reviewService.update(input, reviewId);
		return commentMapper.toResponse(updatedComment);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{novelSlug}/reviews/{reviewId}")
	public void deleteReview(@PathVariable String novelSlug, @PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}


	@GetMapping("/{novelSlug}/ratings")
	public MetricsDTO getNovelMetrics(@PathVariable String novelSlug) {
		return novelRepository.findNovelMetrics(novelSlug);
	}
}