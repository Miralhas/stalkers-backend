package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.CommentDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.input.CommentInput;
import miralhas.github.stalkers.api.dto.input.UpdateCommentInput;
import miralhas.github.stalkers.api.dto_mapper.CommentMapper;
import miralhas.github.stalkers.domain.exception.CommentNotFoundException;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.*;
import miralhas.github.stalkers.domain.utils.CacheManagerUtils;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final CommentRepository commentRepository;
	private final NovelReviewRepository novelReviewRepository;
	private final ChapterReviewRepository chapterReviewRepository;
	private final CommentMapper commentMapper;
	private final NovelService novelService;
	private final ErrorMessages errorMessages;
	private final NovelRepository novelRepository;
	private final ChapterService chapterService;
	private final ValidateAuthorization validateAuthorization;
	private final ChapterRepository chapterRepository;
	private final CacheManagerUtils cacheManagerUtils;

	public List<Comment> findCommentsByParentId(Long id) {
		return commentRepository.findByParentCommentId(id);
	}

	public Comment findCommentByIdOrThrowException(Long id) {
		return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
				errorMessages.get("comment.notFound.id", id)
		));
	}

	@Cacheable(cacheNames = "reviews.list", unless = "#result.getResults().isEmpty()")
	public PageDTO<CommentDTO> findNovelReviewsBySlug(String slug, Pageable pageable) {
		var novelReviewsPage = novelReviewRepository.findRootReviewsByNovelSlug(slug, pageable);
		var commentsDTO = novelReviewsPage.getContent().stream().map(commentMapper::toResponse).toList();
		var pageImpl = new PageImpl<>(commentsDTO, pageable, novelReviewsPage.getTotalElements());
		return new PageDTO<>(pageImpl);
	}

	@Cacheable(cacheNames = "reviews.list", unless = "#result.getResults().isEmpty()")
	public PageDTO<CommentDTO> findChapterReviewsBySlug(String slug, Pageable pageable) {
		var chapterReviewsPage = chapterReviewRepository.findRootReviewsByChapterSlug(slug, pageable);
		var commentsDTO = chapterReviewsPage.getContent().stream().map(commentMapper::toResponse).toList();
		var pageImpl = new PageImpl<>(commentsDTO, pageable, chapterReviewsPage.getTotalElements());
		return new PageDTO<>(pageImpl);
	}

	@Transactional
	public Comment addNovelReview(CommentInput input, String novelSlug) {
		var novel = novelService.findBySlugOrException(novelSlug);
		var user = validateAuthorization.getCurrentUser();
		NovelReview novelReview = commentMapper.fromInputToNovelReview(input);
		var parentComment = novelReview.hasParent() ? findCommentByIdOrThrowException(novelReview.getParentId()) : null;

		novelReview.setParentComment(parentComment);
		if (parentComment == null) {
			// it's a root comment
			novelReview.setNovel(novel);
		}
		novelReview.setCommenter(user);
		novelReview = commentRepository.save(novelReview);
		cacheManagerUtils.evictNovelReviewsEntry(novelSlug);
		return novelReview;
	}

	@Transactional
	public Comment addChapterReview(CommentInput input, String chapterSlug) {
		var chapter = chapterService.findChapterBySlug(chapterSlug);
		ChapterReview chapterReview = commentMapper.fromInputToChapterReview(input);
		var parentComment = chapterReview.hasParent()
				? findCommentByIdOrThrowException(chapterReview.getParentId())
				: null;

		chapterReview.setParentComment(parentComment);
		if (parentComment == null) {
			chapterReview.setChapter(chapter);
		}

		chapterReview.setCommenter(validateAuthorization.getCurrentUser());
		chapterReviewRepository.save(chapterReview);
		cacheManagerUtils.evictNovelReviewsEntry(chapterSlug);
		return chapterReview;
	}

	@Transactional
	public Comment update(UpdateCommentInput input, Long commentId) {
		var comment = findCommentByIdOrThrowException(commentId);
		validateAuthorization.validate(comment.getCommenter());
		comment = commentMapper.update(input, comment);
		comment = commentRepository.save(comment);
		invalidateCommentCacheEntry(comment);
		return comment;
	}

	@Transactional
	public void deleteReview(Long reviewId) {
		var comment = findCommentByIdOrThrowException(reviewId);
		validateAuthorization.validate(comment.getCommenter());
		invalidateCommentCacheEntry(comment);
		if (comment instanceof NovelReview novelReview) {
			Novel novel = novelReview.getNovel();
			if (novel != null) {
				novel.removeReview(novelReview);
				novelRepository.save(novel);
			}
		} else if (comment instanceof ChapterReview chapterReview) {
			Chapter chapter = chapterReview.getChapter();
			if (chapter != null) {
				chapter.removeReview(chapterReview);
				chapterRepository.save(chapter);
			}
		}

		deleteCommentAndChildren(comment.getId());
	}

	// Recursion to delete all children comments
	private void deleteCommentAndChildren(Long commentId) {
		List<Comment> childComments = findCommentsByParentId(commentId);
		for (Comment comment : childComments) {
			deleteCommentAndChildren(comment.getId());
		}
		commentRepository.deleteById(commentId);
	}

	private void invalidateCommentCacheEntry(Comment comment) {
		if (comment instanceof NovelReview novelReview) {
			cacheManagerUtils.evictNovelReviewsEntry(novelReview.getNovel().getSlug());
		} else if (comment instanceof ChapterReview chapterReview) {
			cacheManagerUtils.evictNovelReviewsEntry(chapterReview.getChapter().getSlug());
		}
	}
}
