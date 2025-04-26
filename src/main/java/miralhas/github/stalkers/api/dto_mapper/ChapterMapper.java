package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.ChapterDTO;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface ChapterMapper {
	Chapter fromInput(ChapterInput chapterInput);
	List<Chapter> fromInputCollection(List<ChapterInput> chapterInputList);

	ChapterDTO toResponse(Chapter chapter);

	ChapterSummaryDTO toSummaryResponse(Chapter chapter);

	List<ChapterSummaryDTO> toSummaryCollectionResponse(List<Chapter> chapters);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "slug", ignore = true)
	Chapter update(ChapterInput chapterInput, @MappingTarget Chapter chapter);
}