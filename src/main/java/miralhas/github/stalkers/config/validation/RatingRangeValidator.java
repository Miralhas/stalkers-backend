package miralhas.github.stalkers.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class RatingRangeValidator implements ConstraintValidator<RatingRange, Double> {

	private final List<Double> acceptedValues = new ArrayList<>();

	@Override
	public void initialize(RatingRange constraintAnnotation) {
		acceptedValues.addAll(List.of(0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0));
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		if (value == null) return true;
		var isValid = acceptedValues.contains(value);
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Invalid value. Accepted values are: %s"
							.formatted(acceptedValues))
					.addConstraintViolation();
		}

		return isValid;
	}
}
