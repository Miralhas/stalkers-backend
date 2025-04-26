package miralhas.github.stalkers.domain.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheManagerUtils {

	private final CacheManager cacheManager;

	public void evictSingleEntry(String cacheName, Object key) {
		var cache = cacheManager.getCache(cacheName);
		if (cache != null) cache.evict(key);
	}

	public void evitAllEntries(String cacheName) {
		var cache = cacheManager.getCache(cacheName);
		if (cache != null) cache.clear();
	}

}
