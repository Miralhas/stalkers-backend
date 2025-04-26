package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.NovelDTO;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class NovelMapper {

	@Autowired
	protected NovelRepository novelRepository;

	public abstract Novel fromInput(NovelInput novelInput);

	@Mapping(target = "chaptersCount", expression = "java(chaptersCount(novel))")
	public abstract NovelDTO toResponse(Novel novel);

	public abstract List<NovelDTO> toCollectionResponse(List<Novel> novels);

//	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//	Novel partialUpdate(NovelDTO novelDTO, @MappingTarget Novel novel);

	@Named("chaptersCount")
	long chaptersCount(Novel novel) {
		return novelRepository.countNovelChapters(novel.getId());
	}

}