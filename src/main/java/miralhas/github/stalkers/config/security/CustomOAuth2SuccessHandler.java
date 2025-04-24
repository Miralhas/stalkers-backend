package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.service.AuthenticationService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final UserService userService;
	private final AuthenticationService authenticationService;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException, ServletException {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oauthPrincipal = oauthToken.getPrincipal();
		String email = oauthPrincipal.getAttribute("email");
		String username = oauthPrincipal.getAttribute("name");

		var user = User.builder()
				.email(email)
				.username(username)
				.isOAuth2Authenticated(true)
				.build();

		user = userService.findOrCreateNewUser(user);
		var authenticationResponse = authenticationService.generateTokens(user);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), authenticationResponse);
	}
}
