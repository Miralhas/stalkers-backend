package miralhas.github.stalkers.api.auth.dto;

import miralhas.github.stalkers.api.user.dto.UserDTO;

import java.io.Serial;
import java.io.Serializable;

public record PasswordResetDTO(UserDTO user, String token) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
