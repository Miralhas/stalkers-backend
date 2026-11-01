package miralhas.github.stalkers.domain.shared.event;

public record SendMessageEvent(Object message, String routingKey, String exchange) {}
