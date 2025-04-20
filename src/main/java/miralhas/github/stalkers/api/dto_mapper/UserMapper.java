package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.CreateUserInput;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.domain.model.auth.Role;
import miralhas.github.stalkers.domain.model.auth.User;
import org.mapstruct.*;

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
