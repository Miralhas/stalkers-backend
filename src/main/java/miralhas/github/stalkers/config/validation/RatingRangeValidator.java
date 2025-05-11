package miralhas.github.stalkers.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class RatingRangeValidator implements ConstraintValidator<RatingRange, Double> {

	public static final int RANGE_START = 0;
	public static final int RANGE_END = 9;
	private final List<Double> acceptedValues = new ArrayList<>();

	@Override
	public void initialize(RatingRange constraintAnnotation) {
		for (int i = RANGE_START; i < RANGE_END; i++) {
			acceptedValues.add(i * 0.5 + 1);
		}
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
