package miralhas.github.stalkers.domain.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CacheManagerUtils {

	private final CacheManager cacheManager;
	private final RedisTemplate<String, Object> redisTemplate;

	public void evictSingleEntry(String cacheName) {
		Set<String> keys = redisTemplate.keys(cacheName);
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}

	public void evitAllEntries(String cacheName) {
		var cache = cacheManager.getCache(cacheName);
		if (cache != null) cache.clear();
	}

	public void evictNovelChaptersEntry(String novelSlug) {
		Set<String> keys = redisTemplate.keys("chapters.list::*"+novelSlug+"*");
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}

	public void evictUserLibraryEntry(String userEmail) {
		Set<String> keys = redisTemplate.keys("libraries.list::*"+userEmail+"*");
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}

}
