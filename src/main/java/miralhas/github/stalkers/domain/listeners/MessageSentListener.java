package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miralhas.github.stalkers.domain.event.SendMessageEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSentListener {
	private final RabbitTemplate rabbitTemplate;
	
	@EventListener
	public void onMessageSent(SendMessageEvent event) {
		try {
			rabbitTemplate.convertAndSend(event.exchange(), event.routingKey(), event.message());
		} catch (AmqpException e) {
			log.error("RabbitMQ Failure: {}", e.getMessage());
		}
	}
}
