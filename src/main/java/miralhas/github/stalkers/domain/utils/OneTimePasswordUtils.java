package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class OneTimePasswordUtils {

	public static final int OTP_LENGTH = 6;

	public static String generate() {
		var random = new SecureRandom();
		StringBuilder oneTimePassword = new StringBuilder();
		for (int i = 0; i < OTP_LENGTH; i++) {
			int randomNumber = random.nextInt(10);
			oneTimePassword.append(randomNumber);
		}
		return oneTimePassword.toString().trim();
	}
}
