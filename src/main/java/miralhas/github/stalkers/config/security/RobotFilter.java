package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
public class RobotFilter extends OncePerRequestFilter {

	@Value("${robot.secret}")
	private String robotSecret;

	@Value("${robot.header}")
	private String robotHeader;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {


		if (!Objects.equals(request.getHeader(robotHeader), robotSecret)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("application/json");
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), Map.of("message", "Access denied!"));
			return;
		}

		var authToken = new RobotAuthenticationToken();
		var newContext = SecurityContextHolder.createEmptyContext();
		newContext.setAuthentication(authToken);
		SecurityContextHolder.setContext(newContext);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getHeader("X-Robot-Secret") == null;
	}
}
