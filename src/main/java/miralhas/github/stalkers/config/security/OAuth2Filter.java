package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2Filter extends OncePerRequestFilter {

	private final ErrorMessages errorMessages;
	@Value("${oauth2.google.secret}")
	private String oauth2GoogleSecret;

	@Value("${oauth2.google.header}")
	private String oauth2GoogleHeader;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {

		if (!Objects.equals(request.getHeader(oauth2GoogleHeader), oauth2GoogleSecret)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("application/json");
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), Map.of("message", errorMessages.get("forbidden.notOAuth2")));
			return;
		}

		var authToken = new CustomOAuth2AuthenticationToken();
		var newContext = SecurityContextHolder.createEmptyContext();
		newContext.setAuthentication(authToken);
		SecurityContextHolder.setContext(newContext);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getHeader(oauth2GoogleHeader) == null;
	}
}
