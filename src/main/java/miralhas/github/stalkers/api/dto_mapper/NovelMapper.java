package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.NovelDTO;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.mapstruct.*;

@Mapper(
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING
)
public interface NovelMapper {

	Novel fromInput(NovelInput novelInput);

	NovelDTO toResponse(Novel novel);

//	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//	Novel partialUpdate(NovelDTO novelDTO, @MappingTarget Novel novel);
}