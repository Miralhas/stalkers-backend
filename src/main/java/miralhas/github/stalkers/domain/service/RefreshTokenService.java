package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.RefreshTokenExpiredException;
import miralhas.github.stalkers.domain.exception.RefreshTokenNotFoundException;
import miralhas.github.stalkers.domain.model.auth.RefreshToken;
import miralhas.github.stalkers.domain.model.auth.RefreshTokenRepository;
import miralhas.github.stalkers.domain.model.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

	@Value("${refresh.token.expiration.time}")
	private Long refreshTokenExpirationTime;

	private final RefreshTokenRepository refreshTokenRepository;
	private final MessageSource messageSource;

	public RefreshToken getRefreshTokenOrExcepiton(UUID id) {
		var message = messageSource.getMessage("refreshToken.notFound", new Object[]{id},
				LocaleContextHolder.getLocale()
		);
		return refreshTokenRepository.findById(id)
				.orElseThrow(() -> new RefreshTokenNotFoundException(message));
	}

	@Transactional(noRollbackFor = RefreshTokenExpiredException.class)
	public RefreshToken validateRefreshToken(String refreshTokenToBeValidated) {
		var refreshToken = getRefreshTokenOrExcepiton(UUID.fromString(refreshTokenToBeValidated));
		if (refreshToken.isInvalid()) {
			refreshTokenRepository.deleteAllUserRefreshTokens(refreshToken.getUser());
			refreshTokenRepository.flush();
			var message = messageSource.getMessage(
					"refreshToken.expired",
					new Object[]{refreshToken.getId(), refreshToken.getExpiresAt()},
					LocaleContextHolder.getLocale()
			);
			throw new RefreshTokenExpiredException(message);
		}
		return refreshToken;
	}

	@Transactional
	public RefreshToken save(User user) {
		var refreshToken = RefreshToken.builder()
				.user(user)
				.expiresAt(OffsetDateTime.now().plusSeconds(refreshTokenExpirationTime))
				.build();
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional
	public RefreshToken update(RefreshToken refreshToken, User user) {
		refreshTokenRepository.delete(refreshToken);
		refreshTokenRepository.flush();
		return save(user);
	}
}
