package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserService userService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> getAllUsers() {
		var users = userRepository.findAll();
		return userMapper.toCollectionResponse(users);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public UserDTO updateUser(@RequestBody @Valid UpdateUserInput updateUserInput, JwtAuthenticationToken token) {
		var user = userService.update(updateUserInput, token);
		return userMapper.toResponse(user);
	}
}
