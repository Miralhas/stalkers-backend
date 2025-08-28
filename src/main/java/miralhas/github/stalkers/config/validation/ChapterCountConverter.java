package miralhas.github.stalkers.config.validation;

import miralhas.github.stalkers.api.dto.filter.ChapterCount;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChapterCountConverter implements Converter<String, ChapterCount> {

	@Override
	public ChapterCount convert(String source) {
		if (source == null || !source.contains(":")) {
			return null;
		}
		String[] parts = source.split(":", 2);
		String operator = parts[0].toUpperCase();
		long count = Long.parseLong(parts[1]);

		if (count <= 0) {
			throw new IllegalArgumentException();
		}

		ChapterCount cc = new ChapterCount();
		cc.setOperator(ChapterCount.Operator.valueOf(operator));
		cc.setCount(count);
		return cc;
	}
}
