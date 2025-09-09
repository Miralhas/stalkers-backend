package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.CommentDTO;
import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.api.dto.VoteDTO;
import miralhas.github.stalkers.api.dto.input.CommentInput;
import miralhas.github.stalkers.api.dto.input.UpdateCommentInput;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import miralhas.github.stalkers.domain.model.comment.Vote;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS,
		uses = UserMapper.class
)
public interface CommentMapper {
	@Mapping(target = "parentComment", source = "parentCommentId", qualifiedByName = "longToParentComment")
	@SubclassMapping(source = CommentInput.class, target = NovelReview.class)
	NovelReview fromInputToNovelReview(CommentInput input);

	@Mapping(target = "parentComment", source = "parentCommentId", qualifiedByName = "longToParentComment")
	@SubclassMapping(source = CommentInput.class, target = ChapterReview.class)
	ChapterReview fromInputToChapterReview(CommentInput input);

	@Mapping(target = "parentId", source = "parentComment.id")
	@Mapping(target = "childComments", source = "childComments", qualifiedByName = "childCommentsMapper")
	@Mapping(target = "voters", source = "votes", qualifiedByName = "votersMap")
	@Mapping(target = "type", expression = "java(typeMapper(comment))")
	CommentDTO toResponse(Comment comment);

	@Mapping(target = "type", expression = "java(typeMapper(comment))")
	@Mapping(target = "slug", expression = "java(slugMapper(comment))")
	@Mapping(target = "commenter", source = "commenter.email")
	UserCommentDTO toUserCommentDTO(Comment comment);

	Comment update(UpdateCommentInput input, @MappingTarget Comment comment);

	@Named("longToParentComment")
	default Comment longToParentComment(Long parentId) {
		NovelReview novelReview = new NovelReview();
		novelReview.setId(parentId);
		return novelReview;
	}

	@Named("votersMap")
	default List<VoteDTO> upvotersMap(Set<Vote> votes) {
		if (ObjectUtils.isEmpty(votes)) return List.of();
		return votes.stream().map(v -> new VoteDTO(v.getUser().getEmail(), v.getType())).toList();
	}

	default String typeMapper(Comment comment) {
		if (comment instanceof NovelReview novelReview) {
			return Comment.NOVEL_REVIEW;
		} else if (comment instanceof ChapterReview chapterReview) {
			return Comment.CHAPTER_REVIEW;
		}
		throw new InternalServerError("Unsupported Comment type: " + comment.getClass().getName());
	}

	default String slugMapper(Comment comment) {
		if (comment instanceof NovelReview novelReview) {
			var isRoot = novelReview.getNovel() != null;
			return isRoot ? novelReview.getNovel().getSlug() : null;
		} else if (comment instanceof ChapterReview chapterReview) {
			var isRoot = chapterReview.getChapter() != null;
			return isRoot ? chapterReview.getChapter().getSlug() : null;
		}
		throw new InternalServerError("Unsupported Comment type: " + comment.getClass().getName());
	}

	@Named("childCommentsMapper")
	default List<CommentDTO> childCommentsMapper(List<Comment> childComments) {
		return childComments.stream().map(this::toResponse).toList();
	}
}
