package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomOauth2FailureHandler implements AuthenticationFailureHandler {

	private final ErrorMessages errorMessages;

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
	) throws IOException, ServletException {

		var detail = errorMessages.get("oauth2.failureHandler");
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
		problem.setTitle("OAuth2 Authentication Failure");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setType(URI.create("http://localhost:8080/oauth2-authentication-failure"));

		String problemDetailAsString = new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
				.writeValueAsString(problem);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(problemDetailAsString);
	}
}
