package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.CommentDTO;
import miralhas.github.stalkers.api.dto.input.CommentInput;
import miralhas.github.stalkers.api.dto.input.UpdateCommentInput;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.model.comment.Comment;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import org.mapstruct.*;

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
	CommentDTO toResponse(Comment comment);

	Comment update(UpdateCommentInput input, @MappingTarget Comment comment);

	@Named("longToParentComment")
	default Comment longToParentComment(Long parentId) {
		NovelReview novelReview = new NovelReview();
		novelReview.setId(parentId);
		return novelReview;
	}
	@Named("childCommentsMapper")
	default List<CommentDTO> childCommentsMapper(Set<Comment> childComments) {
		return childComments.stream().map(this::toResponse).toList();
	}
}
