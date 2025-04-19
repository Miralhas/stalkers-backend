package miralhas.github.stalkers.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record UserDTO(
		Long id,
		String username,
		String email,
		Boolean isOAuth2Authenticated,
		List<String> roles
) {
}
