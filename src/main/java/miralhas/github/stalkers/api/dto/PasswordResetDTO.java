package miralhas.github.stalkers.api.dto;

import java.io.Serial;
import java.io.Serializable;

public record PasswordResetDTO(UserDTO user, String token) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
