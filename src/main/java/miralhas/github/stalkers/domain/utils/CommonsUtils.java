package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommonsUtils {

	public static final int WORDS_ARRAY_SIZE = 50;
	public static final List<String> WORDS_ARRAY = List.of(
			"Formula",
			"Evening",
			"Network",
			"Feather",
			"Chapter",
			"Breaker",
			"Auction",
			"Passive",
			"Foreign",
			"Whisper",
			"Measure",
			"Justice",
			"Venture",
			"Captain",
			"Embrace",
			"Cabinet",
			"Desktop",
			"Gravity",
			"Tourist",
			"Factory",
			"Portion",
			"Silence",
			"Rainbow",
			"Freedom",
			"Gesture",
			"Session",
			"Biology",
			"Compass",
			"Surface",
			"Pioneer",
			"Mystery",
			"Imagine",
			"Courage",
			"Meadows",
			"Loyalty",
			"Quantum",
			"Invoice",
			"Holiday",
			"Protein",
			"Mineral",
			"Defense",
			"Machine",
			"Vintage",
			"Fortune",
			"Shelter",
			"Special",
			"Mansion",
			"Publish",
			"Tractor",
			"Context"
	);

	public static final List<String> WORDS_ARRAY_TWO = List.of(
			"Bracket",
			"Village",
			"Scanner",
			"Pottery",
			"Mission",
			"Concert",
			"Dolphin",
			"Monitor",
			"Channel",
			"Pencils",
			"Balance",
			"Surgery",
			"Journey",
			"Texture",
			"Liberty",
			"Glimpse",
			"Program",
			"Flowers",
			"Lecture",
			"Speaker",
			"Comfort",
			"Fantasy",
			"Weather",
			"Witness",
			"Dynamic",
			"Ancient",
			"Gateway",
			"Storage",
			"Library",
			"Maximum",
			"Fishing",
			"Blanket",
			"Pattern",
			"Kitchen",
			"Mustard",
			"Crystal",
			"Sunbeam",
			"Harvest",
			"Diamond",
			"Triumph",
			"Thunder",
			"Organic",
			"Vehicle",
			"Morning",
			"Candles",
			"Railway",
			"Horizon",
			"Pension",
			"Phantom",
			"Pitcher"
	);

	private static final PolicyFactory TEXT_ONLY_POLICY = new HtmlPolicyBuilder()
			.allowElements("p", "br", "b", "strong", "i", "em", "s", "strike", "u")
			.toFactory();

	public String randomUsernameGenerator() {
		var random = new SecureRandom();
		var randomNumber = random.nextInt(WORDS_ARRAY_SIZE);
		var randomNumbers = randomNumbersGenerator(4);
		return WORDS_ARRAY.get(randomNumber) + WORDS_ARRAY_TWO.get(randomNumber) + randomNumbers;
	}

	public String randomNumbersGenerator(int length) {
		var random = new SecureRandom();
		StringBuilder oneTimePassword = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randomNumber = random.nextInt(10);
			oneTimePassword.append(randomNumber);
		}
		return oneTimePassword.toString().trim();
	}

	public String capitalize(String value) {
		return Arrays.stream(value.split("\\s+"))
				.map(StringUtils::capitalize)
				.collect(Collectors.joining(" "));
	}

	public static String getInitialsFromSlug(String slug) {
		String[] words = slug.split("-");

		if (words.length <= 2) {
			return Arrays.stream(words)
				.filter(org.springframework.util.StringUtils::hasText)
				.map(word -> word.length() > 2 ? word.substring(0, 2) : word.substring(0, 1))
				.collect(Collectors.joining());
		}

		return Arrays.stream(slug.split("-"))
				.filter(word -> !word.isEmpty())
				.map(word -> word.substring(0, 1))
				.collect(Collectors.joining());
	}

	public static String sanitize(String dirtyHtml) {
		return TEXT_ONLY_POLICY.sanitize(dirtyHtml);
	}

}
