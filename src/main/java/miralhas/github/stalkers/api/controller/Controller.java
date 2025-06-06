package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> test() {
		var str = CommonsUtils.randomUsernameGenerator();
		return ResponseEntity.ok(Map.of("message", str));
	}
}
