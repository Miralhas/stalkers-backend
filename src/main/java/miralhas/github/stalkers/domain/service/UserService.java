package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.event.SendMessageEvent;
import miralhas.github.stalkers.domain.exception.UserAlreadyExistsException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
	private final ApplicationEventPublisher events;

	public User findUserByEmailOrException(String email) {
		return userRepository.findUserByEmail(email).orElseThrow(() -> {
			var message = errorMessages.get("user.notFound", email);
			return new UsernameNotFoundException(message);
		});
	}

	@Transactional
	public User findOrCreateNewUser(User user) {
		return userRepository
				.findUserByEmail(user.getEmail())
				.orElseGet(() -> create(user));
	}

	@Transactional
	public User create(User user) {
		checkIfUsernameOrEmailAreAvailiable(user);
		var userRole = roleService.getUserRole();
		user.setRoles(Set.of(userRole));
		if (Objects.nonNull(user.getPassword())) user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user);
		events.publishEvent(new SendMessageEvent(
				userMapper.toResponse(user), "rk.password.reset", "stalkers"));
		return user;
	}

	@Transactional
	public User update(UpdateUserInput updateUserInput, JwtAuthenticationToken authToken) {
		var user = findUserByEmailOrException(authToken.getName());
		checkIfCanUpdateUsername(updateUserInput.username(), user);
		userMapper.update(updateUserInput, user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return user;
	}

	private void checkIfCanUpdateUsername(String username, User user) {
		userRepository.findUserByUsername(username).ifPresent(u -> {
			if (!u.getUsername().equals(user.getUsername())) {
				throw new UserAlreadyExistsException(errorMessages.get("user.alreadyExists.username", username));
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