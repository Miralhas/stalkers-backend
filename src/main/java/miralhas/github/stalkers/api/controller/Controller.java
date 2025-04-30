package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.config.security.RobotAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Home")
@RequiredArgsConstructor
public class Controller {

	@GetMapping("/secured")
	public String secured() {
		return "hello, secured";
	}

	@GetMapping("/test")
	public Map<String, String> test() {
		var usr = SecurityContextHolder.getContext().getAuthentication();
		return Map.of("message", "Stalkers API");
	}
}
