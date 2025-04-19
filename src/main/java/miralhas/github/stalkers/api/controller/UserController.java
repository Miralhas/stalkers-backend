package miralhas.github.stalkers.api.controller;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@GetMapping
	public List<UserDTO> getAllUsers() {
		var users = userRepository.findAll();
		return userMapper.toCollectionResponse(users);
	}

}
