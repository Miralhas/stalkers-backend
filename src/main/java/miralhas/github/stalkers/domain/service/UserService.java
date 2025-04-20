package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.exception.UserAlreadyExistsException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ErrorMessages errorMessages;
	private final UserMapper userMapper;

	public User findUserByEmailOrException(String email) {
		return userRepository.findUserByEmail(email).orElseThrow(() -> {
			var message = errorMessages.get("user.notFound", email);
			return new UsernameNotFoundException(message);
		});
	}

	@Transactional
	public User findOrCreateNewUser(User user) {
		return userRepository.findUserByEmail(user.getEmail())
				.orElseGet(() -> {
					var userRole = roleService.getUserRole();
					user.setRoles(Set.of(userRole));
					return userRepository.save(user);
				});
	}

	@Transactional
	public User create(User user) {
		checkIfUsernameOrEmailAreAvailiable(user);
		var userRole = roleService.getUserRole();
		user.setRoles(Set.of(userRole));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user);
		return user;
	}

	@Transactional
	public User update(UpdateUserInput updateUserInput, JwtAuthenticationToken authToken) {
		var user = findUserByEmailOrException(authToken.getName());
		checkIfUsernameIsAvailiable(updateUserInput.username(), user);
		userMapper.update(updateUserInput, user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return user;
	}

	private void checkIfUsernameIsAvailiable(String username, User user) {
		userRepository.findUserByUsername(username).ifPresent(u -> {
			if (!u.getUsername().equals(user.getUsername())) {
				throw new UserAlreadyExistsException(errorMessages.get(
						"user.alreadyExists.username", username), null
				);
			}
		});
	}

	private void checkIfUsernameOrEmailAreAvailiable(User user) {
		Map<String, String> errors = new HashMap<>();

		userRepository.findUserByEmail(user.getEmail())
				.ifPresent(u -> errors.put(
						"email", errorMessages.get("user.alreadyExists.email", u.getEmail())
				));
		userRepository.findUserByUsername(user.getUsername())
				.ifPresent(u -> errors.put(
						"username", errorMessages.get("user.alreadyExists.username", u.getUsername())
				));

		if (!errors.isEmpty()) {
			var message = errorMessages.get("user.alreadyExists");
			throw new UserAlreadyExistsException(message, errors);
		}
	}

}