package miralhas.github.stalkers.api.announcement.mapper;

import miralhas.github.stalkers.api.announcement.dto.AnnouncementDTO;
import miralhas.github.stalkers.api.announcement.dto.input.AnnouncementInput;
import miralhas.github.stalkers.api.announcement.dto.input.UpdateAnnouncementInput;
import miralhas.github.stalkers.api.user.mapper.UserMapper;
import miralhas.github.stalkers.domain.announcement.model.Announcement;
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
		uses = UserMapper.class
)
public interface AnnouncementMapper {

	Announcement fromInput(AnnouncementInput input);

	AnnouncementDTO toResponse(Announcement announcement);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Announcement update(UpdateAnnouncementInput input, @MappingTarget Announcement announcement);
}
