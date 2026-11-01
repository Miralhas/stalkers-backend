package miralhas.github.stalkers.domain.auth.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.auth.dto.PasswordResetDTO;
import miralhas.github.stalkers.api.auth.dto.input.ChangePasswordInput;
import miralhas.github.stalkers.api.user.mapper.UserMapper;
import miralhas.github.stalkers.domain.auth.exception.PasswordResetTokenNotFoundException;
import miralhas.github.stalkers.domain.auth.model.PasswordResetToken;
import miralhas.github.stalkers.domain.shared.event.SendMessageEvent;
import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.auth.repository.PasswordResetTokenRepository;
import miralhas.github.stalkers.domain.user.service.UserService;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import miralhas.github.stalkers.shared.utils.OneTimePasswordUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PasswordResetService {

	private final UserService userService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final ErrorMessages errorMessages;
	private final UserMapper userMapper;
	private final ApplicationEventPublisher events;

	public PasswordResetToken findResetPasswordToken(String token) {
		return passwordResetTokenRepository.findByToken(token)
				.orElseThrow(() -> new PasswordResetTokenNotFoundException(
						errorMessages.get("user.passwordResetToken.notFound", token)
				));
	}

	@Transactional
	public void resetPassword(String resetPasswordToken, ChangePasswordInput changePasswordInput) {
		var token = findResetPasswordToken(resetPasswordToken);
		var user = token.getUser();
		user.setPassword(passwordEncoder.encode(changePasswordInput.newPassword()));
		passwordResetTokenRepository.delete(token);
	}

	@Transactional
	public void createOrUpdateToken(String email) {
		User user =  userService.findUserByEmailOrException(email);
		PasswordResetToken token = passwordResetTokenRepository.findById(user.getId())
				.orElseGet(() -> createResetTokenObject(user));

		token.setToken(OneTimePasswordUtils.generate());

		token = passwordResetTokenRepository.save(token);
		var passwordResetDTO = new PasswordResetDTO(userMapper.toResponse(user), token.getToken());
		events.publishEvent(new SendMessageEvent(passwordResetDTO, "rk.password.reset", "stalkers"));
	}

	private PasswordResetToken createResetTokenObject(User user) {
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setUser(user);
		return resetToken;
	}
}
