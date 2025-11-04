package miralhas.github.stalkers.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private final List<String> acceptedMediaTypes = new ArrayList<>();

	@Override
	public void initialize(FileContentType constraintAnnotation) {
		acceptedMediaTypes.addAll(List.of(
				MediaType.IMAGE_JPEG_VALUE,
				MediaType.IMAGE_PNG_VALUE,
				"image/webp",
				"image/jpg"
		));
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if (value == null) return true;
		var isValid = acceptedMediaTypes.contains(value.getContentType());
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Invalid value. Accepted Media Types are: %s"
							.formatted(acceptedMediaTypes))
					.addConstraintViolation();
		}
		return isValid;
	}
}
