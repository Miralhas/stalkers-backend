package miralhas.github.stalkers.api.exception_handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.exception.ResourceNotFoundException;
import miralhas.github.stalkers.domain.exception.UserAlreadyExistsException;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;
	private final ErrorMessages errorMessages;

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUncaughtException(Exception ex, WebRequest webRequest) {
		log.error("Internal Server Error Exception:", ex);
		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		var detail = errorMessages.get("internalServerError");
		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Internal Server Error");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/internal-server-error"));
		return problemDetail;
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest webRequest) {
		var errors = ex.getErrors();
		var detail = ex.getMessage();
		var status = HttpStatus.CONFLICT;
		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Authentication Conflict");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/authentication-conflict"));
		if (Objects.nonNull(errors)) {
			problemDetail.setProperty("errors", errors);
		}
		return problemDetail;
	}

	@ExceptionHandler(PropertyReferenceException.class)
	public ProblemDetail handlePropertyReferenceException(PropertyReferenceException ex, WebRequest webRequest) {
		var detail = ex.getMessage();
		var status = HttpStatus.BAD_REQUEST;
		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Pagination Bad Request");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/pagination-bad-request"));
		return problemDetail;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest) {
		var detail = errorMessages.get("forbidden");
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, detail);
		problemDetail.setTitle("Forbidden");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/forbidden-access"));
		return problemDetail;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle("Resource Not Found");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/resource-not-found"));
		return problemDetail;
	}

	@ExceptionHandler(BusinessException.class)
	public ProblemDetail handleBusinessException(BusinessException ex, WebRequest webRequest) {
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problemDetail.setTitle("Invalid Request");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/invalid-request"));
		return problemDetail;
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleBadCredentialsException(BadCredentialsException ex, WebRequest webRequest) {
		String detail = errorMessages.get("PasswordComparisonAuthenticator.badCredentials");
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
		problemDetail.setTitle("Invalid Authentication");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/authentication"));
		return problemDetail;
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAuthenticationException(AuthenticationException ex, WebRequest webRequest) {
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
		problemDetail.setTitle("Invalid Authentication");
		problemDetail.setType(URI.create(getBaseUrl(webRequest)+"/errors/authentication"));
		return problemDetail;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		var errorsMap = new HashMap<String, String>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			errorsMap.put(error.getField(), message);
		});
		var detail = errorMessages.get("methodArgumentNotValid");
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
		problemDetail.setTitle("Invalid Fields");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/invalid-fields"));
		problemDetail.setProperty("errors", errorsMap);
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(
			NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		String detail = errorMessages.get("noResourceFound", ex.getResourcePath());
		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Inexistent Resource");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/inexistent-resource"));
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}


	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		String detail = errorMessages.get("httpMethodNotSupported", ex.getMethod(), ex.getSupportedHttpMethods());
		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, detail);
		problemDetail.setTitle("HTTP Method not supported");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/http-method-not-supported"));
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		String detail = errorMessages.get("typeMismatch", ex.getPropertyName(),
				ex.getValue(), ex.getRequiredType().getSimpleName());

		var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
		problemDetail.setTitle("Type Mismatch");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/type-mismatch"));
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}


	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		if (ex.getCause() instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) ex.getCause(), headers, status, request);
		}
		var detail = errorMessages.get("httpMessageNotReadable");

		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Incomprehensible Message");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/incomprehensible-message"));
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(
			InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		String path = joinPath(ex.getPath());
		String detail = errorMessages.get("invalidFormat", path, ex.getValue(), ex.getTargetType().getSimpleName());

		var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle("Invalid Property Format");
		problemDetail.setType(URI.create(getBaseUrl(request)+"/errors/invalid-property-format"));
		return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
	}

	private String joinPath(List<JsonMappingException.Reference> path) {
		return path
				.stream()
				.map(JsonMappingException.Reference::getFieldName)
				.filter(Objects::nonNull).collect(Collectors.joining("."));
	}

	private URI getBaseUrl(WebRequest webRequest) {
		var request = ((ServletWebRequest) webRequest).getRequest();
		var scheme = request.getScheme();
		var serverName = request.getServerName();
		var serverPort = request.getServerPort();
		return URI.create(scheme + "://" + serverName + ":" + serverPort);
	}

}