package miralhas.github.stalkers.api.elders_choice.mapper;

import miralhas.github.stalkers.api.elders_choice.dto.EldersChoiceDTO;
import miralhas.github.stalkers.domain.elders_choice.model.EldersChoice;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface EldersChoiceMapper {

	EldersChoiceDTO toResponse(EldersChoice eldersChoice);
}
