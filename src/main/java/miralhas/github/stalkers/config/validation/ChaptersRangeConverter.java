package miralhas.github.stalkers.config.validation;

import miralhas.github.stalkers.api.dto.filter.ChaptersRange;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChaptersRangeConverter implements Converter<String, ChaptersRange> {

	@Override
	public ChaptersRange convert(String source) {

		if (source == null || !source.contains(":")) {
			return null;
		}

		String[] parts = source.split(":", 2);

		long firstValue = Long.parseLong(parts[0]);
		long secondValue = Long.parseLong(parts[1]);

		if (firstValue < 0 || secondValue < 0) {
			throw new IllegalArgumentException();
		}

		var chaptersRange = new ChaptersRange();
		chaptersRange.setFirstValue(firstValue);
		chaptersRange.setSecondValue(secondValue);

		return chaptersRange;
	}

}
