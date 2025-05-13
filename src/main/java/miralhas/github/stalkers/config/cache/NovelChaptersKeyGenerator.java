package miralhas.github.stalkers.config.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class NovelChaptersKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return StringUtils.arrayToDelimitedString(params, "_");
	}
}
