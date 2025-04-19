package miralhas.github.stalkers.domain.exception;

public class RoleNotFoundException extends ResourceNotFoundException {

  public RoleNotFoundException(String message) {
    super(message);
  }

  public RoleNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}

