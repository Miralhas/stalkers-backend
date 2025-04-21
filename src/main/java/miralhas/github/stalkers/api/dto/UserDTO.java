package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record UserDTO(
		Long id,
		String username,
		String email,
		Boolean isOAuth2Authenticated,
		List<String> roles
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
