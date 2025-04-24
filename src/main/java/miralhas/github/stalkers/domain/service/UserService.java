package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.exception.UserAlreadyExistsException;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "users")
public class UserService  {

	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ErrorMessages errorMessages;
	private final UserMapper userMapper;
	private final ImageService imageService;
	private final ValidateAuthorization validateAuthorization;

	@Cacheable
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findUserByEmailOrException(String email) {
		return userRepository.findUserByEmail(email).orElseThrow(() -> {
			var message = errorMessages.get("user.email.notFound", email);
			return new UsernameNotFoundException(message);
		});
	}

	public User findUserByIdOrException(Long id) {
		return userRepository.findById(id).orElseThrow(() -> {
			var message = errorMessages.get("user.id.notFound", id);
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
	@CacheEvict(allEntries = true)
	public User create(User user) {
		checkIfUsernameOrEmailAreAvailiable(user);
		var userRole = roleService.getUserRole();
		user.setRoles(Set.of(userRole));
		if (Objects.nonNull(user.getPassword())) user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user);
		return user;
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public User update(UpdateUserInput updateUserInput, User user) {
		checkIfCanUpdateUsername(updateUserInput.username(), user);
		userMapper.update(updateUserInput, user);
		if (Objects.nonNull(updateUserInput.password())) user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Transactional
	public Image saveUserImage(User user, Image image, InputStream inputStream) {
		validateAuthorization.validate(user);

		image = user.hasImage() ? imageService.replace(image, user.getImageFileName(), inputStream)
				: imageService.save(image, inputStream);

		user.setImage(image);
		userRepository.saveAndFlush(user);
		return imageService.getImageJsonOrException(image.getId());
	}

	@Transactional
	public void deleteUserImage(User user) {
		validateAuthorization.validate(user);
		imageService.delete(user.getImage());
		user.setImage(null);
		userRepository.save(user);
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