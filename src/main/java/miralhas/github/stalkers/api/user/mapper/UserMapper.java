package miralhas.github.stalkers.api.user.mapper;

import miralhas.github.stalkers.api.user.dto.UserDTO;
import miralhas.github.stalkers.api.auth.dto.input.CreateUserInput;
import miralhas.github.stalkers.api.user.dto.input.UpdateUserInput;
import miralhas.github.stalkers.domain.user.model.Role;
import miralhas.github.stalkers.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {

	User fromInput(CreateUserInput createUserInput);

	@Mapping(target = "roles", qualifiedByName = "mapRoles")
	UserDTO toResponse(User user);

	List<UserDTO> toCollectionResponse(List<User> users);

	@Mapping(target = "id", ignore = true)
	void update(UpdateUserInput userRequest, @MappingTarget User userEntity);

	@Named("mapRoles")
	default String mapRoles(Role role) {
		return role.getName().name();
	}
}
