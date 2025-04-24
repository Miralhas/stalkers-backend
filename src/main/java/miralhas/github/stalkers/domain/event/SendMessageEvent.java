package miralhas.github.stalkers.domain.event;

public record SendMessageEvent(Object message, String routingKey, String exchange) {}
