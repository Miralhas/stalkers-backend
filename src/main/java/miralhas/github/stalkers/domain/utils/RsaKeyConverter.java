package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@UtilityClass
public class RsaKeyConverter {

	public static RSAPublicKey toPublicKey(String key) {
		try {
			String sanitized = key
					.replace("-----BEGIN PUBLIC KEY-----", "")
					.replace("-----END PUBLIC KEY-----", "")
					.replaceAll("\\s", "");

			byte[] decoded = Base64.getDecoder().decode(sanitized);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

			return (RSAPublicKey) KeyFactory
					.getInstance("RSA")
					.generatePublic(spec);

		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid RSA public key", e);
		}
	}

	public static RSAPrivateKey toPrivateKey(String key) {
		try {
			String sanitized = key
					.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "")
					.replaceAll("\\s", "");

			byte[] decoded = Base64.getDecoder().decode(sanitized);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

			return (RSAPrivateKey) KeyFactory
					.getInstance("RSA")
					.generatePrivate(spec);

		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid RSA private key", e);
		}
	}
}