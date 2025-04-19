package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class CustomAccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
		var detail = "error";
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, detail);
		problem.setTitle("Forbidden");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setType(URI.create("http://localhost:8080/forbidden-access"));
		String problemDetailAsString = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_ABSENT).writeValueAsString(problem);
		response.setStatus(problem.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(problemDetailAsString);
	}

}
