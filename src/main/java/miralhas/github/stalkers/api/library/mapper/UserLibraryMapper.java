package miralhas.github.stalkers.api.library.mapper;

import miralhas.github.stalkers.api.library.dto.UserLibraryDTO;
import miralhas.github.stalkers.domain.library.model.UserLibrary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface UserLibraryMapper {

	@Mapping(target = "chapterSlug", source = "currentChapter.slug")
	@Mapping(target = "chapterTitle", source = "currentChapter.title")
	@Mapping(target = "novelSlug", source = "novel.slug")
	@Mapping(target = "novelTitle", source = "novel.title")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "totalChapters", ignore = true)
	@Mapping(target = "libraryElementId", source = "id")
	@Mapping(target = "chapterNumber", source = "currentChapter.number", defaultValue = "0L")
	UserLibraryDTO toResponse(UserLibrary userLibrary);

}
