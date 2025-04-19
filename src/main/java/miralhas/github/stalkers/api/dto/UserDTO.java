package miralhas.github.stalkers.api.dto;

import java.util.List;

public record UserDTO(
		Long id,
		String username,
		String email,
		Boolean isOAuth2Authenticated,
		List<String> roles
) {
}
