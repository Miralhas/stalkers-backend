package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.api.swagger.UserControllerSwagger;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

	private final UserMapper userMapper;
	private final UserService userService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Override
	public List<UserDTO> getAllUsers() {
		var users = userService.findAll();
		return userMapper.toCollectionResponse(users);
	}

	@PatchMapping
	@ResponseStatus(HttpStatus.OK)
	@Override
	public UserDTO updateUser(@RequestBody @Valid UpdateUserInput updateUserInput, JwtAuthenticationToken token) {
		var user = userService.findUserByEmailOrException(token.getName());
		user = userService.update(updateUserInput, user);
		return userMapper.toResponse(user);
	}
}
