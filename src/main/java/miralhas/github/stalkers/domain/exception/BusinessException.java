package miralhas.github.stalkers.domain.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

  private Map<String, String> errors;

  public BusinessException() {
    super();
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message,  Map<String, String> errors) {
    super(message);
    this.errors = errors;
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(String message, Throwable cause, Map<String, String> errors) {
    super(message, cause);
    this.errors = errors;
  }


  public BusinessException(Throwable cause) {
    super(cause);
  }

  protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

