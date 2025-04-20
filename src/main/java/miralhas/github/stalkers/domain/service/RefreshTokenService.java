package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.config.properties_metadata.TokenPropertiesConfig;
import miralhas.github.stalkers.domain.exception.RefreshTokenExpiredException;
import miralhas.github.stalkers.domain.exception.RefreshTokenNotFoundException;
import miralhas.github.stalkers.domain.model.auth.RefreshToken;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.RefreshTokenRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

	private final TokenPropertiesConfig tokenPropertiesConfig;
	private final RefreshTokenRepository refreshTokenRepository;
	private final ErrorMessages errorMessages;

	public RefreshToken getRefreshTokenOrExcepiton(UUID id) {
		var message = errorMessages.get("refreshToken.notFound", id);
		return refreshTokenRepository.findById(id)
				.orElseThrow(() -> new RefreshTokenNotFoundException(message));
	}

	@Transactional(noRollbackFor = RefreshTokenExpiredException.class)
	public RefreshToken validateRefreshToken(String refreshTokenToBeValidated) {
		var refreshToken = getRefreshTokenOrExcepiton(UUID.fromString(refreshTokenToBeValidated));
		if (refreshToken.isInvalid()) {
			refreshTokenRepository.deleteAllUserRefreshTokens(refreshToken.getUser());
			refreshTokenRepository.flush();
			var message = errorMessages.get(
					"refreshToken.expired", refreshToken.getId(), refreshToken.getExpiresAt()
			);
			throw new RefreshTokenExpiredException(message);
		}
		return refreshToken;
	}

	@Transactional
	public RefreshToken save(User user) {
		var refreshToken = RefreshToken.builder()
				.user(user)
				.expiresAt(OffsetDateTime.now().plusSeconds(tokenPropertiesConfig.refreshToken().expirationTime()))
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
