package miralhas.github.stalkers.domain.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.List;

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

	public static String getInitialsFromSlug(String slug) {
		String[] words = slug.split("-");
		StringBuilder initials = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				initials.append(word.charAt(0));
			}
		}
		return initials.toString();
	}

}
