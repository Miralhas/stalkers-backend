package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.ChapterDTO;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import org.mapstruct.*;

@Mapper(
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ChapterMapper {
	Chapter fromInput(ChapterDTO chapterDTO);

	ChapterDTO toResponse(Chapter chapter);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Chapter partialUpdate(ChapterDTO chapterDTO, @MappingTarget Chapter chapter);
}