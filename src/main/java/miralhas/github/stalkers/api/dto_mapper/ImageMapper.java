package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.ImageDTO;
import miralhas.github.stalkers.api.dto.input.ImageInput;
import miralhas.github.stalkers.domain.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface ImageMapper {

	@Mapping(target = "fileName", source = "file.originalFilename")
	@Mapping(target = "contentType", source = "file.contentType")
	@Mapping(target = "size", source = "file.size")
	Image fromInput(ImageInput imageInput);

	ImageDTO toResponse(Image image);
}
