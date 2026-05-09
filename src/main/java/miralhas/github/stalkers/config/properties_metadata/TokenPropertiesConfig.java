package miralhas.github.stalkers.config.properties_metadata;

import lombok.Getter;
import lombok.Setter;
import miralhas.github.stalkers.domain.utils.RsaKeyConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Getter
@Setter
@ConfigurationProperties(prefix = "authentication.token")
public class TokenPropertiesConfig {

	public record AuthenticationToken(Long expirationTime) {}

	AuthenticationToken accessToken;
	AuthenticationToken refreshToken;
	String privateKey;
	String publicKey;

	public RSAPublicKey getRsaPublicKey() {
		return RsaKeyConverter.toPublicKey(publicKey);
	}

	public RSAPrivateKey getRsaPrivateKey() {
		return RsaKeyConverter.toPrivateKey(privateKey);
	}
}
