package miralhas.github.stalkers.domain.auth.exception;

import miralhas.github.stalkers.domain.shared.exception.BusinessException;

public class RefreshTokenExpiredException extends BusinessException {

  public RefreshTokenExpiredException(String message) {
    super(message);
  }

  public RefreshTokenExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

}
