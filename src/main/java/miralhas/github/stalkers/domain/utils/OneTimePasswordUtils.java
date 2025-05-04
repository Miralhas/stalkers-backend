package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OneTimePasswordUtils {

	public static final int OTP_LENGTH = 6;

	public static String generate() {
		return CommonsUtils.randomNumbersGenerator(OTP_LENGTH);
	}
}
