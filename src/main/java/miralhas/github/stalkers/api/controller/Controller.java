package miralhas.github.stalkers.api.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping()
public class Controller {

	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public String home(@RequestParam(required = false) Integer number) {
		System.out.println("got here");
		System.out.println(number);
		return "hello";
	}

	@GetMapping("/secured")
	public String secured() {
		return "hello, secured";
	}
}
