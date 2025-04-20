package miralhas.github.stalkers.domain.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorMessages {
	private final MessageSource messageSource;

	public String get(String code, @Nullable Object... args) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
}
