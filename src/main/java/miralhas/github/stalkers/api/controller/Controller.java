package miralhas.github.stalkers.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home")
@RequiredArgsConstructor
public class Controller {

	@GetMapping("/secured")
	public String secured() {
		return "hello, secured";
	}
}
