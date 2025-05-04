package miralhas.github.stalkers.config.rabbitmq;

import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {

	@Bean
	public SimpleMessageConverter messageConverter() {
		var simpleMessageConverter = new SimpleMessageConverter();
		simpleMessageConverter.setAllowedListPatterns(getAllowedListPatterns());
		return simpleMessageConverter;
	}

	private List<String> getAllowedListPatterns() {
		return List.of(
				"miralhas.github.stalkers.api.dto.*",
				"java.*"
		);
	}
}
