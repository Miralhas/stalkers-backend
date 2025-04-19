package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.UserAlreadyExistsException;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final MessageSource messageSource;

	public User findUserByEmailOrException(String email) {
		return userRepository.findUserByEmail(email).orElseThrow(() -> {
			var message = messageSource.getMessage(
					"user.notFound", new Object[]{email}, LocaleContextHolder.getLocale()
			);
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

	private void checkIfUsernameOrEmailAreAvailiable(User user) {
		Map<String, String> errors = new HashMap<>();

		userRepository.findUserByEmail(user.getEmail())
				.ifPresent(u -> errors.put("email", messageSource.getMessage(
						"user.alreadyExists.email", new Object[]{u.getEmail()}, LocaleContextHolder.getLocale()
				)));
		userRepository.findUserByUsername(user.getUsername())
				.ifPresent(u -> errors.put("username", messageSource.getMessage(
						"user.alreadyExists.username", new Object[]{u.getUsername()}, LocaleContextHolder.getLocale()
				)));

		if (!errors.isEmpty()) {
			throw new UserAlreadyExistsException(errors);
		}
	}

}
