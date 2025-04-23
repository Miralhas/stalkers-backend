package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home")
@RequiredArgsConstructor
public class Controller {

	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public String home() {
		return "Stalkers API";
	}

	@GetMapping("/secured")
	public String secured() {
		return "hello, secured";
	}
}
