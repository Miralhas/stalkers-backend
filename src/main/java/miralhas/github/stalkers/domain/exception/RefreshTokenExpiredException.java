package miralhas.github.stalkers.domain.exception;

public class RefreshTokenExpiredException extends BusinessException {

  public RefreshTokenExpiredException(String message) {
    super(message);
  }

  public RefreshTokenExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

}
