package miralhas.github.stalkers.api.auth.dto;

public record AuthenticationDTO(
		String accessToken,
		String refreshToken,
		Long accessTokenExpiresIn,
		Long refreshTokenExpiresIn
) {
}
