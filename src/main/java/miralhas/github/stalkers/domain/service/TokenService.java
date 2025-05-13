package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.config.properties_metadata.TokenPropertiesConfig;
import miralhas.github.stalkers.domain.model.auth.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

	private final TokenPropertiesConfig tokenPropertiesConfig;
	private final JwtEncoder jwtEncoder;
	private final UserMapper userMapper;

	public Jwt generateToken(User user) {
		Instant now = Instant.now();
		var mappedUser = userMapper.toResponse(user);
		var scopes = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("stalkers")
				.subject(user.getEmail())
				.issuedAt(now)
				.expiresAt(now.plusSeconds(tokenPropertiesConfig.accessToken().expirationTime()))
				.claim("user", mappedUser)
				.claim("scope", scopes)
				.build();
		var header = JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims));
	}
}