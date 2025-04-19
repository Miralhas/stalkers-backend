package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.dto.input.SigninInput;
import miralhas.github.stalkers.domain.security.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	@Value("${access.token.expiration.time}")
	private Long accessTokenExpirationTime;

	@Value("${refresh.token.expiration.time}")
	private Long refreshTokenExpirationTime;

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final RefreshTokenService refreshTokenService;

	@Transactional
	public AuthenticationDTO authenticate(SigninInput signinInput) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(signinInput.email(), signinInput.password());
		var authenticationResult = authenticationManager.authenticate(authenticationToken);
		var user = ((SecurityUser) authenticationResult.getPrincipal()).getUser();
		SecurityContextHolder.getContext().setAuthentication(authenticationResult);
		var jwt = tokenService.generateToken(user);
		var refreshToken = refreshTokenService.save(user);
		return new AuthenticationDTO(
				jwt.getTokenValue(),
				refreshToken.getId().toString(),
				accessTokenExpirationTime,
				refreshTokenExpirationTime
		);
	}
}
