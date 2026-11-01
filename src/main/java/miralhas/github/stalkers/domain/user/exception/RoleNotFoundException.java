package miralhas.github.stalkers.domain.user.exception;

import miralhas.github.stalkers.domain.shared.exception.ResourceNotFoundException;

public class RoleNotFoundException extends ResourceNotFoundException {

  public RoleNotFoundException(String message) {
    super(message);
  }

  public RoleNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}

