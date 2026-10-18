package miralhas.github.stalkers.api.dto;

public record AuthenticationDTO(
		String accessToken,
		String refreshToken,
		Long accessTokenExpiresIn,
		Long refreshTokenExpiresIn
) {
}
