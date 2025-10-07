package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.dto.input.SigninInput;
import miralhas.github.stalkers.config.properties_metadata.TokenPropertiesConfig;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.security.SecurityUser;
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final TokenPropertiesConfig tokenPropertiesConfig;
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	@Transactional
	public AuthenticationDTO authenticate(SigninInput signinInput) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(signinInput.email(), signinInput.password());
		var authenticationResult = authenticationManager.authenticate(authenticationToken);
		var user = ((SecurityUser) authenticationResult.getPrincipal()).getUser();
		SecurityContextHolder.getContext().setAuthentication(authenticationResult);
		return generateTokens(user);
	}

	@Transactional
	public AuthenticationDTO generateTokens(User user) {
		var jwt = tokenService.generateToken(user);
//		var refreshToken = refreshTokenService.save(user);
		return new AuthenticationDTO(
				jwt.getTokenValue(),
//				refreshToken.getId().toString(),
				tokenPropertiesConfig.accessToken().expirationTime()
//				tokenPropertiesConfig.refreshToken().expirationTime()
		);
	}

	@Transactional
	public AuthenticationDTO authenticateOAuth2(String email) {
		String username = CommonsUtils.randomUsernameGenerator();
		var user = User.builder()
				.email(email)
				.username(username)
				.isOAuth2Authenticated(true)
				.build();
		user = userService.findOrCreateNewUser(user);
		return generateTokens(user);
	}

}
