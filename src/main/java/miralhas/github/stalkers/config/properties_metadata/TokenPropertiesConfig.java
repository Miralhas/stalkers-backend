package miralhas.github.stalkers.config.properties_metadata;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "authentication.token")
public record TokenPropertiesConfig (
		AuthenticationToken accessToken,
		AuthenticationToken refreshToken,
		JwtProperty jwt
) {
	public record JwtProperty (RSAPrivateKey privateKey, RSAPublicKey publicKey) {}
	public record AuthenticationToken (Long expirationTime) {}
}
