package miralhas.github.stalkers.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public String home() {
		return "hello";
	}

	@GetMapping("/secured")
	public String secured() {
		return "hello, secured";
	}
}
