package miralhas.github.stalkers.api.series.mapper;

import miralhas.github.stalkers.api.novel.mapper.NovelMapper;
import miralhas.github.stalkers.api.series.dto.SeriesDTO;
import miralhas.github.stalkers.api.series.dto.SeriesSummaryDTO;
import miralhas.github.stalkers.api.series.dto.input.SeriesInput;
import miralhas.github.stalkers.api.series.dto.input.UpdateSeriesInput;
import miralhas.github.stalkers.domain.series.model.Series;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS,
		uses = NovelMapper.class
)
public interface SeriesMapper {
	Series fromInput(SeriesInput input);

	SeriesDTO toResponse(Series series);

	SeriesSummaryDTO toSummaryResponse(Series series);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Series update(UpdateSeriesInput input, @MappingTarget Series series);
}
