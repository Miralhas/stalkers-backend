package miralhas.github.stalkers.config.redis;

import miralhas.github.stalkers.config.cache.NovelChaptersKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(StringRedisSerializer.UTF_8);
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(connectionFactory);
		template.setEnableTransactionSupport(true);
		return template;
	}

	@Bean("novelChaptersKeyGenerator")
	public KeyGenerator keyGenerator() {
		return new NovelChaptersKeyGenerator();
	}

}
